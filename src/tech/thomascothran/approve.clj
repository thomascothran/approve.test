(ns tech.thomascothran.approve
  (:require [clojure.test :refer [deftest]]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.pprint :as pp])
  (:import [java.io File]))

;; (defn def-validation
;;   [test-name & body]
;;   (deftest test-name))

(def default-config
  {:path-root "./test/validate"})

(def ^:dynamic *config*
  default-config)

(defmulti get-previous-value
  (fn [store _test-ns _validation-name]
    (if (keyword? store)
      store
      (type store))))

(defmulti set-new-value
  (fn [store _test-ns _validation-name]
    (if (keyword? store)
      store
      (type store))))

(defn diff
  "Returns nil if validation fails, else returns [previous-x, x]"
  ([validation-name x]
   (diff *config* *ns* validation-name x))
  ([config test-ns validation-name x]
   (let [path (str/join File/separator
                        [(:path-root config)
                         test-ns])
         previous-x (some-> (slurp path)
                            (edn/read-string)
                            validation-name)]
     (if (and previous-x (not= previous-x x))
       [previous-x x]
       nil))))

(comment
  (diff :my-validator [:a :b :c]))

(defmacro env-printer
  []
  (pp/pprint &form)
  (pp/pprint &env))

(defn foo
  "I don't do a whole lot."
  [x]
  (env-printer)
  (prn x "Hello, World!"))

(comment
  (foo 1))
