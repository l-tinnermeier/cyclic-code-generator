
public class DecoderLogic {
	
	static final int INPUT_LENGTH = 15;
	static final int REGISTERS_COUNT = 4;
	
	static int input_bit = 0b110101000101010;
	static int[] input = new int[15];
	
	// XOR R0 R1 R2 XOR R3
	static int[] registers = new int[4];
	static int currentBit = 15;
	
	public static void main(String args[]) {
		
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
	}
	
	
	private static void clockCycle() {
		if (currentBit <= 0) return;
		
		int[] oldRegisters = registers.clone();
		int incoming = input[currentBit - 1];
		int feedback = oldRegisters[3];
		
		registers[0] = incoming ^ feedback;
		registers[1] = oldRegisters[0];
		registers[2] = oldRegisters[1];
		registers[3] = oldRegisters[2] ^ feedback;
		
		currentBit--;
	}
	
	private static void printRegisters() {
		for (int i = 0; i < REGISTERS_COUNT; i ++) {
			System.out.print(registers[i] + " ");
		}
		System.out.println();
	}
	

}
