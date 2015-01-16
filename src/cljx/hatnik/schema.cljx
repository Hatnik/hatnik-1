(ns hatnik.schema
  (:require [schema.core :as s]))

(defn string-of-length
  "Create schema that validates string length."
  [min max]
  (s/both s/Str
          (s/pred #(<= min (count %) max)
                  (symbol (str "length-from-" min "-to-" max "?")))))

(def Id
  "Schema for validaing that string matches id.
  We restrict ids to be up to 32 symbols consisting only from
  alphanumeric values. MongoDB uses such ids."
  (s/both (string-of-length 1 32)
          (s/pred #(re-matches #"^[a-zA-Z0-9]+$" %)
                  'alphanumeric?)))

(def Library
  "Schema for validating libraries."
  (string-of-length 1 128))

(def TemplateBody
  "Schema for validating templates that will be used as message bodies.
  For example in email or github issues."
  (string-of-length 1 2000))

(def TemplateTitle
  "Schema for validating templates that will be used as message titles.
  For example in email or github issues."
  (string-of-length 1 256))

(def GithubRepository
  (s/both (s/pred #(re-matches #"(?i)^[A-Z0-9-_.]+/[A-Z0-9-_.]+$" %)
                  'valid-github-repo?)
          (string-of-length 1 128)))

(def ReplaceOperation
  "Schema for replact operation in pull request action."
  {:file (string-of-length 1 1024)
   :regex (string-of-length 1 128)
   :replacement (string-of-length 1 128)})

(def PredefinedOperations
  "List of harcoded operations that can be used instead of manually
  setting them up."
  (s/enum "project.clj"))

(def Project
  "Schema for project. Project has only 1 field - name in API."
  {:name (string-of-length 1 128)})

(def EmailAction
  {:project-id Id
   :library Library
   :type (s/eq "email")
   :subject TemplateTitle
   :body TemplateBody})

(def NoopAction
  {:project-id Id
   :library Library
   :type (s/eq "noop")})

(def GithubIssueAction
  {:project-id Id
   :library Library
   :type (s/eq "github-issue")
   :title TemplateTitle
   :body TemplateBody
   :repo GithubRepository})

(def GithubPullRequestAction
  {:project-id Id
   :library Library
   :type (s/eq "github-pull-request")
   :title TemplateTitle
   :body TemplateBody
   :repo GithubRepository
   :operations (s/either [ReplaceOperation]
                         PredefinedOperations)})

(def Action
  "Schema for action. Essentially it is the union of all actions."
  (s/conditional
   #(= (:type %) "email") EmailAction
   #(= (:type %) "noop") NoopAction
   #(= (:type %) "github-issue") GithubIssueAction
   #(= (:type %) "github-pull-request") GithubPullRequestAction))

(comment

  (s/check Id "32")

  )

