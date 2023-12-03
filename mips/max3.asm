# MAX3
# Takes three integers from user input. Uses a subroutine called max3, which takes the maximum of
# three integers and returns which is greatest. Inside max3, uses a subroutine called max2,
# which takes in two integers and returns whichever is greater. Finds the max2 of the first two
# integers. Then, returns the max2 of the result and the third integer. Prints the result.
# @author Nelson Gou
# @version 11/17/23

.data
	prompt: .asciiz "Enter an integer: "
	result: .asciiz "The greatest integer is "

.text

main:
	# take user input into $t0
	li $v0 4
	la $a0 prompt
	syscall
	li $v0 5
	syscall
	move $t0 $v0
	
	# take user input into $a1
	li $v0 4
	la $a0 prompt
	syscall
	li $v0 5
	syscall
	move $a1 $v0
	
	# take user input into $v0
	li $v0 4
	la $a0 prompt
	syscall
	li $v0 5
	syscall
	
	# max3(x, y, z) caller
	move $a0 $t0	# prepare arguments
	move $a2 $v0
	subu $sp $sp 4
	sw $ra ($sp)	# save $ra into stack
	jal max3
	lw $ra ($sp)	# load $ra from stack
	addu $sp $sp 4
	
	# print result
	move $t0 $v0
	li $v0 4
	la $a0 result
	syscall
	li $v0 1
	move $a0 $t0
	syscall
	
	# normal termination
	li $v0 10
	syscall

max3:
	# max3(x, y, z) callee
	
	# max2(x, y) caller
	subu $sp $sp 4
	sw $ra ($sp)	# save $ra into stack
	jal max2
	lw $ra ($sp)	# load $ra from stack
	addu $sp $sp 4
	
	move $a0 $v0	# let xy = max2(x, y)
	move $a1 $a2
	
	# max2(xy, z) caller
	subu $sp $sp 4
	sw $ra ($sp)	# save $ra into stack
	jal max2
	lw $ra ($sp)	# load $ra into stack
	addu $sp $sp 4
	
	jr $ra
	
max2:
	# max2(x, y) callee: COPIED FROM MAX2.ASM
	
	ble $a0 $a1 else
	# x > y
	move $v0 $a0
	jr $ra
else:	# x <= y
	move $v0 $a1
	jr $ra
