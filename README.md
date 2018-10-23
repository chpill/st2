# st2

st2 (STatic STyling) is an experiment to allow defining styles with clojure and
exporting CSS for the browser and JS styles declaration for react-native.

EXTREMELY ALPHA SOFTWARE, you have been warned.


### Limitations

For the style data to be available during compilation, it must be both in cljc AND required before launching the compilation. This will imply using a clj namespace to compile the code (for development and production).

### devolopment

Using clojure tools.deps

`clojure -A:fig:cider -m nrepl.cmdline --middleware '["cider.nrepl/cider-middleware", "cider.piggieback/wrap-cljs-repl"]'`


Then, to launch cljs compilation through figwheel, type `(go!)` into the REPL
