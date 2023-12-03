.text

main:
	li $v0, 31
	li $a0, 60
	li $a1, 2000
	li $a2, 0
	li $a3, 127
	syscall
	
	li $a0, 64
	syscall
	
	li $a0, 67
	syscall
	
	li $v0, 32
	li $a0, 2000
	syscall
	
	li $v0, 31
	li $a0, 60
	li $a1, 2000
	li $a2, 0
	li $a3, 127
	syscall
	
	li $a0, 64
	syscall
	
	li $a0, 67
	syscall
	
	li $v0, 10
	syscall