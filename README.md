# st2

st2 (STatic STyling) is an experiment to allow defining styles with clojure and
exporting CSS for the browser and JS styles declaration for react-native.

EXTREMELY ALPHA SOFTWARE, you have been warned.


### devolopment

Using clojure tools.deps

`clojure -A:fig:cider -m nrepl.cmdline --middleware '["cider.nrepl/cider-middleware", "cider.piggieback/wrap-cljs-repl"]'`


Then, to launch cljs compilation through figwheel, type `(go!)` into the REPL
