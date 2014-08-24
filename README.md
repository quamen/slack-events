# slack-events

:warning: **This is a work in progress, and my first Clojure project** :warning:

A slack integration for keeping track of meetup.com events.

Based loosely off of [this tutorial](http://udayv.com//clojure/2014/08/21/writing-hooks-for-slack-in-clojure/)

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Setup

This project uses [environ](https://github.com/weavejester/environ)
to manage environment variables while developing.
You should create a `profiles.clj` file with the following values
filled in:

```clj
{:dev
 {:env
  {:slack-url ""
   :meetup-api-key ""
   :slack-token ""}}}
```

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2014 Gareth Townsend
