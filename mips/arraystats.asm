# ARRAY STATISTICS
# The program prompts the user for ten numbers through user input. It then prints out the
# sum, average, minimum, and maximum of those ten numbers.
#
# @author Nelson Gou
# @version 11/7/23

.data
	prompt: .asciiz "Enter a number: "
	sum: .asciiz "The sum is: "
	avg: .asciiz "\nThe average is: "
	minPrint: .asciiz "\nThe min is: "
	maxPrint: .asciiz "\nThe max is: "

.text

main:
	li $t0, 1		# $t0 is the number of numbers read
	li $t1, 0		# $t1 is the sum
	sll $t2, $t0, 31	# $t2 is the max and is set to the smallest integer value by default
	subu $t3, $t2, 1	# $t3 is the min and is set to the largest integer value by default
	
loop:
	# if 10 numbers have been read, jump to end
	bgt $t0, 10, end
	
	# prompt and take user input
	li $v0, 4
	la $a0, prompt
	syscall		# print prompt
	li $v0, 5
	syscall		# take user input, saved in $v0
	
	# update statistics
	addu $t0, $t0, 1	# increment # of numbers
	addu $t1, $t1, $v0	# add the new number to the sum ($t1 += $v0)
	bgt $v0, $t2, setMax	# if the new number is greater than the current max ($v0 > $t2), jump to setMax
cont1:	blt $v0, $t3, setMin	# if the new number is less than the current min ($v0 < $t3), jump to setMin

	# continue looping
cont2:	j loop

setMax:
	move $t2, $v0	# $v0 is the new max, so set max to its value ($t2 = $v0)
	j cont1		# go back to the loop

setMin:
	move $t3, $v0	# $v0 is the new min, so set min to its value ($t3 = $v0)
	j cont2		# go back to the loop

end:
	# print sum
	li $v0, 4
	la $a0, sum
	syscall		# print sum text
	li $v0, 1
	move $a0, $t1
	syscall		# print the sum ($t1) itself
	
	# print average text
	li $v0, 4
	la $a0, avg
	syscall
	
	# calculate average by dividing the sum by number of numbers (10)
	li $t4, 10	# $t4 is the number of numbers (10)
	div $t1, $t4	# calculate average by taking sum/# of numbers
	mflo $a0	# set average to $a0
	li $v0, 1
	syscall		# print average
	
	# print max
	li $v0, 4
	la $a0, maxPrint
	syscall		# print max text
	li $v0, 1
	move $a0, $t2	# print the max ($t2) itself
	syscall
	
	# print min
	li $v0, 4
	la $a0, minPrint
	syscall		# print min text
	li $v0, 1
	move $a0, $t3	# print the min ($t3) itself
	syscall
	
	# normal termination
	li $v0, 10
	syscall
