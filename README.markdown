# clarsec

clarsec is an attempt to port Haskell Parsec to Clojure.

This library is inspired on http://kotka.de/projects/clojure/parser.html and uses the Meikel Brandmeyer's "monad" library (slightly adapted), as I didn't understand how the Clojure contrib monad
library works.

## Usage

There is an example parser which I ported straight from the Haskell Parsec code. Basically, you should be able to write the examples on http://kotka.de/projects/clojure/parser.html. The library comes with a small number of basic combinators. I hope it will be useful.

## Caveats

Forward references are a mess. I tried to maintain the DSL as simple as possible. Imagine that `structureDef` and `comma` are two already existing
parsers, and `brackets` and `sepBy` are two combinators. I want to be able to write it like this:

~~~~clojure
(def structure 
     (brackets (sepBy structureDef comma)))
~~~~

However, clojures `def` binding is strict and all the referenced vars have to be already defined, otherwise you get:

    Var example/structureDef is unbound.

I'm a clojure newbie, so I don't know exactly how to avoid this. I know only of two workarounds:

~~~~clojure
;; use functions
(defn structure [] (brackets (sepBy (structureDef) (comma))))

;; use delay
(def structure (delay (brackets (sepBy structureDef comma))))
~~~~

I opted for the `delay`, as it allowed me to avoid all those spurious parens, and retain the illusion of a parser DSL, and put the `delay` only where needed. (or everywhere
with a `defblabla` macro).

Unfortunately this required a small patch to Brandmeyer's monad library.

## Installation

    leim jar

## License

http://www.apache.org/licenses/LICENSE-2.0.html
