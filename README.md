# build_systems_a_la_carte

This project is an attempt to translate the code and the concepts of the paper
[Andrey Mokhov, Neil Mitchell, and Simon Peyton Jones. 2018. Build Systems à la Carte. Proc. ACM Program.
Lang. 2, ICFP, Article 79 (September 2018)](https://www.microsoft.com/en-us/research/uploads/prod/2018/03/build-systems.pdf)
from Haskell to Scala 3.
Useful references have been: 

- [the code for the original article](https://github.com/snowleopard/build)
- [a similar attempt in Scala 2](https://github.com/DavidGregory084/forge) by @DavidGregory084

The project is meant for self-study, and, as I am not a Scala expert, much of it will seem 
questionable at best, and needs heavy refactoring.
The code organization is also not ideal.
The primary goal was to arrive at something that could run in Scala and replicate some tests 
mentioned in the paper, at the same time making the translation as close to the original as possible
(e.g.: Store was written in functional style, like in the Haskell code, even if an OO implementation
would have been more idiomatic in Scala).
The secondary goal is to arrive at a good level of conciseness (even if it is likely not possible 
to match Haskell). 

# Personal impressions

The difficulties for me were many, mainly related to:

 - my scarce experience in Scala, and only a theoretical knowledge of Haskell
 - no experience with Monad transformers
 - the differences between Haskell and Scala type inference; many times I had to add type annotations 
   to avoid errors, or while trying to find the cause of an unrelated problem
 - terminology differences between Haskell and Scala Cats (it took a while to figure out that
   MonadState equivalent in Cats has been renamed to Stateful)
 - the fact that MonadState in Haskell is also a Monad, while Stateful is not

On the other hand this exercise is (and hopefully will be) very helpful for me to learn more
about functional programming. Polite comments and suggestions are welcome.

# Status

Currently only 'busy' and 'make' build systems have been implemented, with minimal testing.

