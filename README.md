## Form Flow Starter App ##

This is a standard Spring Boot application that uses Form Flow Builder tools as a library. It
contains an example of a simple, generic application for public benefits. An applicant can input
their personal information, upload supporting documents, and receive a confirmation email with a
filled-in application PDF.

The example application can be viewed [here](https://example.com).

This code includes a form flow generated using this tool, a method for filling out an example PDF,
and sending a confirmation email. The content of the example flow is defined in both English and 
Spanish.

Out-of-the-box, integration with the following third-party services are available:

- Google Analytics
- Mixpanel
- Optimizely
- Intercom
- Google Ads
- Facebook Ads

These are configurable in `application.yaml`.

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