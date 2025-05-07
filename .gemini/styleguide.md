# Introduction

This style guide outlines the coding conventions for Java code developed at Liferay.

# Key Principals

* **Readability:** Code should be easy to understand for all team members.
* **Maintainability:** Code should be easy to modify and extend.
* **Consistency:** Adhering to a consistent style across all projects improves
  collaboration and reduces errors.
* **Performance:** While readability is paramount, code should be efficient.

# Additional Details

## Line Length

* **Maximum line Length:** 80 characters.

## Variables

* Variable declarations should be in alphabetical order
* If multiple variables are collected as values of a map, each variable's name should match the key used in the collection
* Variable names for collections should have the collection type as a suffix to the variable name (e.g., `JSONArray` should be named `myJSONArray`, `ArrayList` should be named `myList`)
* Variables of the generic type `Object` should have the name `object` as part of the variable name

## Methods

* Avoid loops with large method bodies by delegating to a function instead
* Method parameters should be listed in alphabetical order

## Classes

* Aside from `_log`, avoid declaring static final variables if they are only referenced once, and instead instantiate them where they are used

## Log messages

* Log messages should start with verbs in present progressive (e.g., Adding, Modifying, Deleting)
* Proper nouns in log messages should be listed in alphabetical order
