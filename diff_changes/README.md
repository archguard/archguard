# Diff Changes

Increment AST check.

Algorithm:

1. diff file size and change time ?
2. diff package ast
3. compare AST by files ?

MVP：

1. diff changes between two commits
2. diff changes between multiple commits

Impl:

1. create based version AST
2. collect changed files to new AST node
3. collection related classes and functions


Diff Class

1. if class is same do nothing
2. compare for class body
   1. if compare for functions by orders
   2. compare field for changes
