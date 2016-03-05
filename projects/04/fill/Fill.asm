// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// Put your code here.

(INFINITELOOP)
    // Initialize address variable to Base Address of the Screen Map
	@SCREEN
	D=A
	@address
	M=D // address = 16384 (base address of the Hack screen)

    // Initialize variable n = 8192
    @8192
    D=A
	@n
	M=D

	// Initialize variable i = 0
	@i
	M=0

	// Read the status of the Keyboard.
	@KBD
	D=M
	@WHITESCREEN
	D;JEQ

	(BLACKSCREEN)
		// if (i == n) go to END
		@i
		D=M
		@n
		D=D-M
		@END
		D;JEQ

		// Set the row to -1.
		@address
		A=M
		M=-1

        // Increment i by 1
		@i
		M=M+1

		// Increment to the next Screen Map address
		@address
		M=M+1
		
		// Repeat the inner loop.
		@BLACKSCREEN
		0;JMP

	(WHITESCREEN)
	    // if (i == n) go to END
		@i
		D=M
		@n
		D=D-M
		@END
		D;JEQ

		// Set the row to 0.
        @address
        A=M
        M=0

        // Increment i by 1
		@i
		M=M+1

		// Increment to the next Screen Map address
		@address
		M=M+1
		
		// Repeat the inner loop.
		@WHITESCREEN
		0;JMP

// Repeat the inifinite loop.
(END)
@INFINITELOOP
0;JMP
