# approve.test 🌟

![approve.test](./doc/approval_test_img.png)

An intuitive and lightweight approval testing library for Clojure, seamlessly integrated with `clojure.test`.

**Why approve.test?** 🚀
- 📚 **Simplicity First**: Designed for Clojure developers new to approval testing. Easy to learn and quick to implement!
- 💾 **EDN Snapshot Storage**: Leverages pretty-printed EDN for storing test snapshots, embracing Clojure's strengths.
- 🔗 **Seamless Integration**: Automatically hooks into `clojure.test`, ensuring a frictionless testing workflow.
- 🪶 **Lightweight**: A minimalistic and efficient alternative to Java-based approval testing frameworks.
- 🛠️ **Extensible**: Customize to fit your unique testing scenarios.
- 🌱 **Status: Alpha**: Actively developed, inviting contributions and feedback!

## What is Approval Testing? 🤔

Approval Testing is a testing technique where the test output is compared against a pre-approved snapshot. It’s especially useful for testing legacy systems. If the output matches the snapshot, the test passes; if not, it fails.

## Usage 🛠️

The core function in `approve.test` is `verify`. Here's how to get started:

```clojure
(ns a.test.namespace
  (:require [clojure.test :refer [deftest testing is]]
            [tech.thomascothran.approve.test.alpha 
             :refer [verify]]))

(deftest my-test
  (let [sorted-names (sort ["Charles" "Alice" "Diane" "Ben"])]
    (verify :correctly-sorted default-config sorted-names))
```

This creates a snapshot file at `test-resources/approve/a/test/namespace/my-test/correctly-sorted.received.edn`. Initially, manually review and rename it to `correctly-sorted.accepted.edn`. Future tests will compare against this snapshot.

## Extensibility through Multimethods 🔄

`approve.test` is highly extensible via multimethods. You have the flexibility to tailor the following aspects:

- 🔄 **Serialization Format**: Not a fan of EDN? Choose a format that suits your needs.
- 🗂️ **Storage**: Prefer storing your snapshots somewhere other than a file? Customize the storage medium.
- 🛣️ **Paths**: Want to change where snapshots are stored? Easily modify the paths to fit your directory structure.

## License

Copyright © 2023 Thomas Cothran

Distributed under the Eclipse Public License version 1.0.
