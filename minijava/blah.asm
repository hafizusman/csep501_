; Listing generated by MuhammadUsman Non-optimizing Compiler Version <>

TITLE	blah.asm

	.686P 
	.XMM  
	include listing.inc 
	.model	flat 

INCLUDELIB LIBCMT 
INCLUDELIB OLDNAMES 

PUBLIC	_asm_main 
EXTRN	_put:PROC 
EXTRN  _get:PROC 
EXTRN  _mjmalloc:PROC 

_DATA	SEGMENT
BadContravariantParams$$:
	DD 0	;  no base class
	DD _asm_main	;  BadContravariantParams::main
B$$:
	DD 0	;  no base class
	DD B$my2	;  B::my2
	DD B$my	;  B::my

_DATA	ENDS

_TEXT	SEGMENT 

_asm_main PROC	; main
; Line 3
push	ebp
mov	ebp, esp
; Line 3
push	4
call	_mjmalloc
add	esp, 4
lea	edx, B$$
mov	[eax], edx
mov	ecx, eax
push	ecx
; Line 3
mov	eax, [ecx]
call	dword ptr [eax+4]
push	0
call	_put
add	esp, 4
mov	esp, ebp
pop	ebp
ret
_asm_main ENDP	; main

_TEXT	ENDS
END 
