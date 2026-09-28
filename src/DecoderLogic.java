
public class DecoderLogic {

	static final int INPUT_LENGTH = 15;
	static final int REGISTERS_COUNT = 4;
	static final int[] SYNDROME_TABLE = buildSyndromeTable();

	static int input_bit = 0b100101011100111;
	static int[] input = new int[15];
	static int[] output = new int[15];

	// XOR R0 R1 R2 XOR R3
	static int[] registers = new int[4];
	static int currentBit = 15;
    private static boolean loaded;
    private static String receivedCodeword = "";
    private static int lastInput, lastFeedback, lastLeftXor, lastRightXor;
    private static int[] pending;
    private static int lastR2;

    public static void loadCodeword(String codeword) {
        if (codeword == null || !codeword.matches("[01]{15}")) {
            throw new IllegalArgumentException("Expected exactly 15 binary digits");
        }
        receivedCodeword = codeword;
        input_bit = Integer.parseInt(codeword, 2);
        for (int i = 0; i < INPUT_LENGTH; i++) input[i] = codeword.charAt(i) - '0';
        registers = new int[REGISTERS_COUNT];
        currentBit = INPUT_LENGTH;
        lastInput = lastFeedback = lastLeftXor = lastRightXor = lastR2 = 0;
        pending = null;
        output = new int[INPUT_LENGTH];
        loaded = true;
    }

    public static int[] getRegisters() { return registers.clone(); }
    public static int getClock() { return INPUT_LENGTH - currentBit; }
    public static boolean isFinished() { return loaded && currentBit == 0; }
    public static String getReceivedCodeword() { return receivedCodeword; }
    public static int getLastInput() { return lastInput; }
    public static int getLastFeedback() { return lastFeedback; }
    public static int getLastLeftXor() { return lastLeftXor; }
    public static int getLastRightXor() { return lastRightXor; }
    public static int getSyndrome() {
        if (!isFinished()) throw new IllegalStateException("Process all 15 bits first");
        return registers[0] | (registers[1] << 1) | (registers[2] << 2) | (registers[3] << 3);
    }
    public static int getErrorPosition() { return SYNDROME_TABLE[getSyndrome()]; }



	public static void main(String args[]) {
        // Optional command-line codeword; the GUI calls loadCodeword directly.
        loadCodeword(args.length > 0 ? args[0]
                : String.format("%15s", Integer.toBinaryString(input_bit)).replace(' ', '0'));

		for (int i = (INPUT_LENGTH - 1); i >= 0; i--) {
			int bit = (input_bit >> i) & 1;
			System.out.print(bit + " ");
			input[INPUT_LENGTH - (i + 1)] = bit;
		}

		System.out.println();

		for (int i = 0; i < INPUT_LENGTH; i++) {
			System.out.print(input[i] + " ");
		}

		System.out.println();


		for (int i = 0; i < REGISTERS_COUNT; i ++) {
			System.out.print(registers[i] + " ");
		}

		System.out.println();
		System.out.println();

		for (int i = 0; i < INPUT_LENGTH; i++) {
			clockCycle();
			printRegisters();
			System.out.println();
		}

		correctError();
	}


    public static boolean hasPreview() { return pending != null; }
    public static int[] getNextRegisters() { return pending == null ? null : pending.clone(); }
    public static void prepareNext() {
        if (!loaded || currentBit <= 0 || pending != null) return;
        int[] old = registers.clone();
        lastInput = input[currentBit - 1];
        lastFeedback = old[3];
        lastR2 = old[2];
        lastLeftXor = lastInput ^ lastFeedback;
        lastRightXor = old[2] ^ lastFeedback;
        pending = new int[] {lastLeftXor, old[0], old[1], lastRightXor};
    }
    public static void commitNext() {
        if (pending == null) return;
        registers = pending;
        pending = null;
        currentBit--;
    }
    public static void clockCycle() { prepareNext(); commitNext(); }
    public static String getCalculations() {
        return "Input " + lastInput + " XOR old R3 " + lastFeedback + " = " + lastLeftXor
             + "     |     Old R2 " + lastR2 + " XOR old R3 " + lastFeedback + " = " + lastRightXor;
    }
    public static int getLastR2() { return lastR2; }

	public static int calculateSyndrome(int receivedWord) {
		int[] r = new int[4];

		for (int pos = 0; pos < INPUT_LENGTH; pos++) {
			int incoming = (receivedWord >> pos) & 1;
			int[] old = r.clone();

			r[0] = incoming ^ old[3];
			r[1] = old[0];
			r[2] = old[1];
			r[3] = old[2] ^ old[3];
		}

	    return r[0] | (r[1] << 1) | (r[2] << 2) | (r[3] << 3);
	}

    public static void correctError() {
        int errorPosition = getErrorPosition(); // Requires all 15 clocks.
        output = input.clone();
        if (errorPosition >= 0) output[INPUT_LENGTH - 1 - errorPosition] ^= 1;
    }
    public static String getCorrectedCodeword() {
        correctError();
        StringBuilder word = new StringBuilder(INPUT_LENGTH);
        for (int bit : output) word.append(bit);
        return word.toString();
    }
    public static String getRecoveredMessage() { return getCorrectedCodeword().substring(4); }

    /** Pure correction helper for the automatic test; does not disturb the active simulation. */
    public static String correctWord(String word) {
        if (word == null || !word.matches("[01]{15}")) throw new IllegalArgumentException("Expected 15 bits");
        int value = Integer.parseInt(word, 2);
        int syndrome = calculateSyndrome(value);
        if (syndrome != 0) value ^= 1 << SYNDROME_TABLE[syndrome];
        return String.format("%15s", Integer.toBinaryString(value)).replace(' ', '0');
    }


	private static int[] buildSyndromeTable() {
		int[] table = new int[16];
		java.util.Arrays.fill(table, -1);

		for (int pos = 0; pos < INPUT_LENGTH; pos++) {
			int errorPattern = 1 << pos;
			int syndrome = calculateSyndrome(errorPattern);

			if (syndrome == 0 || table[syndrome] != -1) {
				throw new IllegalStateException("Single-bit syndromes must be nonzero and unique");
			}

			table[syndrome] = pos;
		}

		return table;
	}

	private static void printRegisters() {
		for (int i = 0; i < REGISTERS_COUNT; i ++) {
			System.out.print(registers[i] + " ");
		}
		System.out.println();
	}


}
