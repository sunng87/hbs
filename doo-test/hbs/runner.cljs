(ns hbs.runner
  (:require [cljs.test :as test]
            [doo.runner :refer-macros [doo-tests]]
            [hbs.core-test]))

(enable-console-print!)
(doo-tests 'hbs.core-test)
