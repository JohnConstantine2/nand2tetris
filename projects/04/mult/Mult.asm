// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.

// The number of loops to go through.
@R0
D=M
@n
M=D

// Initialize variable i to zero.
@i
M=0

// Initialize variable sum to zero.
@sum
M=0

(LOOP)
    // if (i == n) goto END
    @i
    D=M
    @n
    D=D-M
    @STOP
    D;JEQ

    // Accumulate the sum.
    @R1
    D=M
    @sum
    M=M+D

    // Icremement i by 1.
    @i
    M=M+1

    // Repeat the loop.
    @LOOP
    0;JMP

(STOP)
    @sum
    D=M
    @R2
    M=D // RAM[2] = sum

(END)
    @END
    0;JMP

