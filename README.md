## Form Flow Starter App ##

This is a standard Spring Boot application that uses Form Flow Builder tools as a library.

## Form Flow Concepts ##

* Flows
* Inputs
* Screens
* Conditions

Flows are the top-level construct. A flow has many inputs to receive user data (e.g. first name, zip
code, email). A flow also has many screens, which can be made up of one or more inputs. A flow has
a ordering of screens, and can use conditions to skip screens. Conditions can also be used on
individual screens to show or hide content.

## Defining A Flow ##

Flows are defined in YAML, in `resources/flows`. An example is in `resources/flows/apply.yaml`. 
It looks like this:

```yaml
apply:
  - screen1
  - screen2:
    nextPages:
      - screen3:
          condition: showScreen3
```

```java
public enum Conditions {
  showScreen3 -> appliedForSnap && appliedForCcap,
} 
```

## Defining Inputs ##

Inputs are defined in YAML, in `resources/inputs`. An example is in `resources/inputs/apply.yaml`.

```yaml
apply:
  - firstName
    type: TEXT
    validation: REQUIRED
  - lastName
    type: TEXT
    validation: REQUIRED
```

```java

class ApplyInputs
  TextInput firstName;
  EmailInput email;
  PhoneInput phone;
end

```

## Defining Screens ##

Screens are defined as HTML using the Thymeleaf templating engine.

## Defining Conditions ##

Conditions are defined in Java.