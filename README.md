# GoNature Park Management System

GoNature is a Java-based application designed with JavaFX for the graphical user interface and utilizes the OCSF (Object Client-Server Framework) for effective client-server communication. This system is structured around the MVC (Model-View-Controller) architecture, enhancing the management of park operations. Developed within the Eclipse IDE as part of an academic project, GoNature addresses the complex needs of managing park facilities and services.

## System Architecture

- **Package Structure**:
  - **gui**: Contains all JavaFX classes that handle the graphical user interface.
  - **client**: Includes classes for client-side operations and communication with the server.
  - **server**: Houses server-side classes responsible for managing connections, data, and business logic.
  - **entities**: Defines all the entity classes used within the application, representing data like users, bookings, and park parameters.

- **Development Environment**: The application is developed and maintained using the Eclipse IDE, which provides robust support for Java and JavaFX projects.

## System Design and Planning

Before the coding phase commenced, significant planning and system design were carried out to ensure a deep understanding of the system requirements and to effectively plan the project structure. This initial phase included the creation of various UML diagrams and detailed use case scenarios, critical for visualizing the overall system architecture and interactions. These included:

- **Use Case Diagrams**: Define and visualize interactions between users and the system.
- **Class Diagrams**: Establish the structure of the system including classes, their attributes, methods, and relationships.
- **Activity Diagrams**: Outline the flow of activities involved in the processes within the system.
- **Sequence Diagrams**: Detail the sequence of operations that occur between system components.

## System Requirements

- **Java**: JDK 11 or later
- **JavaFX**: For the UI components
- **MySQL**: Database installation is required
- **OCSF**: To handle the client-server communication
- **Eclipse IDE**: Recommended for development and debugging

## Setup and Installation

### Prerequisites
- **MySQL Installation**: Ensure MySQL is installed and operational on your system. Set up a GoNature-specific database and execute the provided SQL scripts to establish the required tables and initial data.

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/GoNature.git
   cd GoNature
   ```

2. **Import the project into Eclipse**:
   - Open Eclipse and select `File` > `Import` > `Existing Projects into Workspace`.
   - Navigate to the cloned directory and import the project.

3. **Compile and run the project**:
   - In Eclipse, right-click on the project in the Project Explorer, select `Run As` > `Java Application` for both the server and client applications.

## Employee Roles

### Roles and Responsibilities
- **Park Manager**:
  - Modify park parameters such as capacity, allowed stay duration, and the number of orders.
  - Generate reports regarding park operations.
  
- **Department Manager**:
  - Approve changes to park parameters requested by the Park Manager.
  - Prepare monthly reports on visits and cancellations.
  
- **Service Worker**:
  - Register new guides into the system.
  
- **Park Employee**:
  - Manage the entry and exit of visitors.
  - Verify availability and approve entry for uninvited visitors if there is available space.

## Visitor Management

- **Existing Reservation Check**: Visitors with a prior booking log in using their identity card number to manage their reservation.
- **New Visitor Booking**: First-time visitors are taken directly to the visit reservation page if they don't have a booking.
- Availability checks for requested dates and times.
- Option to join a waiting list if the initial booking time is unavailable.
- Notification and booking confirmation for customers on the waiting list if an opening becomes available.

## Project Demonstration Video

Watch a detailed demonstration of the GoNature system in action below:

![GoNature](https://github.com/yossishemtov/SemesterProjectGoNature/assets/146210244/aa0fd794-df7c-4486-b27e-bd1288ec44d4)

## Database Interaction

GoNature interacts with a MySQL database for storing and retrieving all operational data including user details, visitor bookings, and administrative changes. Ensuring the database is correctly configured is crucial for the system’s functionality.

## Contributing

Interested in contributing? Great! Here's how you can help:

1. **Fork the repository**:
   - Make a copy on your GitHub account and start experimenting.

2. **Create a branch**:
   - Use the following command to create a new branch for your feature:
     ```bash
     git checkout -b feature-yournewfeature
     ```

3. **Commit your changes**:
   - Make your changes and commit them with a clear, descriptive message:
     ```bash
     git commit -am 'Add some feature'
     ```

4. **Push to the branch**:
   - Push your changes to your GitHub repository:
     ```bash
     git push origin feature-yournewfeature
     ```

5. **Submit a Pull Request**:
   - Go to your repository on GitHub, select your branch, and click on the 'New pull request' button. Follow the instructions to open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

---

We hope you find the GoNature park management system useful and look forward to seeing how it can be adapted and expanded to meet the needs of various park management scenarios!
