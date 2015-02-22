(ns hatnik.db.storage)

(defprotocol UserStorage
  "Protocol for storing users."
  (get-user [storage email] "Returns user if exists on nil.")
  (get-user-by-id [storage id] "Returns user by id.")
  (create-user! [storage data] "Creates new user and returns id.")
  (update-user! [storage email data] "Updates user."))

(defprotocol ProjectStorage
  "Protocol for storing projects."
  (get-projects [storage user-id] [storage] "Returns list of all projects for given user or simply all project. Version with one argument should be used only in by workers.")
  (get-project [storage id] "Returns project for id. Should be used only in worker server.")
  (create-project! [storage data] "Creates project. Returns id.")
  (update-project! [storage user-id id data] "Updates project.")
  (delete-project! [storage user-id id] "Deletes project."))

(defprotocol ActionStorage
  "Protocol for storing actions."
  (get-actions [storage] [storage user-id project-id]
    "Returns list of all actions or list of actions for given project and
user. The version with single argument should never be used on web server.")
  (create-action! [storage user-id data] "Creates action. Returns id.")
  (update-action! [storage user-id id data] "Updates action.")
  (delete-action! [storage user-id id] "Deletes action."))
