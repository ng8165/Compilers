# SIMPLE PROGRAM (EXERCISE 2)
# This simple program loads 2 and 3 into temporary registers and stores their sum
# into another register. It then terminates normally.
#
# @author Nelson Gou
# @version 11/7/23

.text

main:
	li $t0, 2		# load 2 into $t0
	li $t1, 3		# load 3 into $t1
	addu $t2, $t0, $t1	# store $t0 + $t1 in $t2
	li $v0, 10		# normal termination
	syscall
