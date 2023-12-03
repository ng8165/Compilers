.text

main:
	# read an integer and save in $t0
	li $v0, 5
	syscall
	move $t0, $v0
	
	# read an integer and save in $t1
	li $v0, 5
	syscall
	move $t1, $v0
	
	li $t2, 0
