Reslting Test
===============

This is the primary testing module. It is designed to handle functional tests and other heavyweight testing.

The basic pattern is to write a minimal application as a subproject of the `wars` subproject. Then you create a test class
inheriting from `WarProjectTests`, passing in the simple name of the subproject. Then you write some tests to show your
stuff works. Then&mdash;*BOOM!*&mdash;you are Internet famous.
