(ns thomascothran.approve.test.alpha-test
  (:require [clojure.test :refer [deftest testing is]
             :as t]
            [tech.thomascothran.approve.test.alpha :as a]))

(deftest ima-deftest
  (testing "a"
    (testing "b"
      (is (= 1 2)))))

(comment
  (ima-deftest))

(deftest test-vars->path
  (let [test-vars [#'thomascothran.approve.test.alpha-test/ima-deftest]

        expected ["approve" "approve-test" "ima-deftest"]]
    (is (= expected
           (#'a/test-vars->path test-vars)))))

(comment
  (test-vars->path))

(deftest test->path
  (testing "a"
    (testing "b"
      (is (= ["thomascothran" "approve" "test" "alpha-test" "test->path"]
             (#'a/->path)))))

  (testing "with explicit bindings"
    (let [test-vars [#'thomascothran.approve.test.alpha-test/ima-deftest]
          test-context '("b" "a")
          expected  ["thomascothran" "approve" "test" "alpha-test" "ima-deftest"]]
      (is (= expected
             (#'a/->path test-vars test-context)))))

  (testing "with validation"
    (a/validate :path-should-not-change (#'a/->path))))

(comment
  (test->path))
