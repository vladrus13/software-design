package ru.vladrus13.bean

open class Token

open class OperationToken : Token()

open class PlusOperation : OperationToken()
open class MinusOperation : OperationToken()
open class MultiplyOperation : OperationToken()
open class DivideOperation : OperationToken()

open class BracketToken : Token()

open class OpenBracket : BracketToken()
open class CloseBracket : BracketToken()

open class NumberToken(val number : Long) : Token()