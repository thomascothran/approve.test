(ns approve.approve-test
  (:require [clojure.test :refer [deftest testing is]
             :as t]
            [tech.thomascothran.approve.alpha :as a]))

(deftest ima-deftest
  (testing "a"
    (testing "b"
      (is (= 1 2)))))

(comment
  (ima-deftest))

(deftest test-vars->path
  (let [test-vars [#'approve.approve-test/ima-deftest]
        expected ["approve" "approve-test" "ima-deftest"]]
    (is (= expected
           (#'a/test-vars->path test-vars)))))

(comment
  (test-vars->path))

(deftest test->path
  (testing "a"
    (testing "b"
      (is (= ["approve" "approve-test" "test->path" "a" "b"]
             (#'a/->path)))))

  (testing "with explicit bindings"
    (let [test-vars [#'approve.approve-test/ima-deftest]
          test-context '("b" "a")
          expected  ["approve" "approve-test" "ima-deftest"]]
      (is (= expected
             (#'a/->path test-vars test-context)))))

  (testing "with validation"
    (a/validate :path-should-not-change (#'a/->path))))

(comment
  (test->path))
