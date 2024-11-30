# IntelliJ Fluent Builder Plugin

The **IntelliJ Fluent Builder Plugin** is your go-to solution for generating and managing fluent builders in Java projects.

## Key Features

1. **Effortless Fluent Builder Creation**  
   Quickly generate fluent builders for your classes with minimal effort, following a clean and intuitive design.

2. **Highly Adaptable Builders**  
   Customize your builder to fit your exact needs:
    - Modify parameter types or names.
    - Remove parameters to simplify your builder.
    - Make parameters optional to suit different use cases.
    - Reorder parameters for better readability and usability.  
      The plugin automatically adjusts the builder interfaces and methods to reflect your changes, ensuring a consistent and reliable builder.

3. **Seamless Compatibility with Existing Code**  
   The plugin adheres to established naming conventions to ensure compatibility with your existing project:
    - **Builder Class Names**: `ClassNameBuilder`
    - **Builder Interface Names**: `ClassNameParamBuilder`  
      This ensures that your generated builders integrate seamlessly into your current workflow.

## Hexagonal Architecture Support

This plugin is built with **Hexagonal Architecture** in mind, allowing you to:

- **Export the Domain**: The plugin facilitates the export of your domain logic, allowing you to define and refine your model without worrying about infrastructure concerns.
- **Adapt the Primary Infrastructure**: You can adapt your primary infrastructure to match the specific needs of the domain.
- **Generate Commands**: Once your domain is exported and infrastructure is adapted, the plugin generates a list of ordered commands to perform the necessary actions to create your fluent builder.

## Need Help or Have Suggestions?

Feel free to reach out.
