# GUESS THE NUMBER, COMPUTER EDITION
# The program asks the user to choose a random number from 1 to 100. It generates guesses
# using binary search, then repeatedly prompts the user if the guess was too low, too high,
# or correct. If the user says the guess was correct, the program terminates normally.
#
# @author Nelson Gou
# @version 11/7/23

.data
	intro: .asciiz "Choose a random number from 1 to 100. I will try to guess it.\nIf my guess is too low, type 0. If it's too high, type 1. If it's correct, type 2. Let's begin!\n\n"
	guessText1: .asciiz "My guess is "
	guessText2: .asciiz ". Is that too low, too high, or correct? "
	success: .asciiz "Yay!"

.text

main:
	# print intro
	li $v0, 4
	la $a0, intro
	syscall
	
	# set up for binary search. low is $t0, high is $t1, and $t3 = 2.
	li $t0, 1
	li $t1, 100
	li $t3, 2
	
loop:
	# calculate mid and store in $a0
	addu $t2, $t0, $t1
	div $t2, $t3
	mflo $t2
	
	# print guessText
	li $v0, 4
	la $a0, guessText1
	syscall		# print guessText1
	li $v0, 1
	move $a0, $t2
	syscall		# print mid ($t2)
	li $v0, 4
	la $a0, guessText2
	syscall		# print guessText2
	
	# read user input (stored in $v0)
	li $v0, 5
	syscall
	
	beqz $v0, low		# user said 0 (guess was too low), so jump to low
	beq $v0, 1, high	# user said 1 (guess was too high), so jump to high
	
	# user didn't say 0 or 1, so must have said 2. print success
	li $v0, 4
	la $a0, success
	syscall
	
	# normal termination
	li $v0, 10
	syscall

low:
	# guess was too low, so need to change low to mid+1 ($t0 = $t2 + 1)
	addu $t0, $t2, 1
	
	j loop # continue looping

high:
	# guess was too high, so need to change high to mid-1 ($t1 = $t2 - 1)
	subu $t1, $t2, 1
	
	j loop # continue looping
