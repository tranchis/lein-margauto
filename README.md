# lein-margauto

Leiningen Plugin for [Marginalia](https://github.com/fogus/marginalia)
that watches your source directories for changes to your clojure
source files and rebuilds the Marginalia documentation whenever you
update your source code.

## Installation

Add to your `project.clj`:

```clojure
:plugins [...
           [com.github.tranchis/lein-margauto "1.0.14"]
         ]
```

## Usage

    lein margauto [some/path/dir]

Open `uberdoc.html` from the path you set up, or from `docs/` by default.
Refresh as you make changes to see the updates.

## Configuration

```clojure
    (defproject my-project "1.0.0"
      ...
      :margauto {
        :src-dirs    ["src" "test"]
        :sleep-time  1000
        :target-dir  "resources/public"})
```

### `:src-dirs`

By default `lein-margauto` only searches `src/` for files that end in
`.clj`.  You can change this behavior by setting `:src-dirs` to a
vector of directories to search.

### `:sleep-time`

`lein-margauto` uses a brute force appraoch to detecting changes in
your sources.  It performs a recursive search through the `:src-dirs`
rebuilding when it sees a change.  `:sleep-time` controls how long
`lein-margauto` will pause before checking the directory structure
again.

The default is 1000 ms (1 second).

### `:target-dir`

The relative path where the `uberdoc.html` file will be generated.

## License

Copyright (C) 2011 Kyle R. Burton <kyle.burton@gmail.com>
          (C) 2013 Sergio Alvarez-Napagao <tranchis@gmail.com>

Distributed under the Eclipse Public License, the same as Clojure.
