# NUMBER LOOP (EXERCISE 6)
# This program prompts the user for a lower bound, an upper bound, and a step. It then iterates
# from the lower bound to the higher bound with the step and prints every number it sees
# along the way.
#
# @author Nelson gou
# @version 11/7/23

.data
	promptLower: .asciiz "Enter a lower bound: "
	promptUpper: .asciiz "Enter a upper bound: "
	promptStep: .asciiz "Enter a step: "
	separator: .asciiz " "

.text

main:
	# print lower bound prompt
	li $v0, 4
	la $a0, promptLower
	syscall
	
	# take user input for low bound and store in $t0. this will also represent the current number later.
	li $v0, 5
	syscall
	move $t0, $v0
	
	# print upper bound prompt
	li $v0, 4
	la $a0, promptUpper
	syscall
	
	# take user input for upper bound and store in $t1
	li $v0, 5
	syscall
	move $t1, $v0
	
	# print step prompt
	li $v0, 4
	la $a0, promptStep
	syscall
	
	# take user input for step and store in $t2
	li $v0, 5
	syscall
	move $t2, $v0
	
loop:
	# if current number is greater than upper bound ($t0 > $t1), jump to after
	bgt $t0, $t1, after
	
	# print current number ($t0)
	li $v0, 1
	move $a0, $t0
	syscall
	
	# print separator (a space)
	li $v0, 4
	la $a0, separator
	syscall
	
	# increase current number by step ($t0 += $t2)
	addu $t0, $t0, $t2
	
	# continue looping
	j loop

after:
	# normal termination
	li $v0, 10
	syscall
