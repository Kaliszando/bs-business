<!-- PROJECT LOGO -->
<div align="center">
<h3><img src="https://github.com/Kaliszando/bs-web-client/blob/main/src/assets/img/favicon-16x16.png" alt="Logo" width="16" height="16"> BugStalker</h3>
</div>

<p>
BugStalker is a bug tracking system, that addresses the problem of ticket management in software development teams.
It presents the design of a system that supports the implementation phase of the software development life cycle by
streamlining the methods of presenting the status of issues, managing a large number of them and enabling the division 
of labor and responsibilities. The system was implemented as a web application in a client-server architecture.
</p>

![BacklogScreenshot](https://github.com/Kaliszando/bs-web-client/blob/main/src/assets/img/backlog.png)

BugStalker consists of three, separate projects:
* [bs-business](https://github.com/Kaliszando/bs-business) backend
* [bs-web-client](https://github.com/Kaliszando/bs-web-client) frontend
* [bs-api-specification](https://github.com/Kaliszando/bs-api-specification) API specification and generation tool


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
        <li><a href="#additional-libraries">Additional Libraries</a></li>
        <li><a href="#technologica-challenges">Technological Challenges</a></li>
        <li><a href="#uml-diagrams">UML diagrams</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

Backend is responsible for providing main business logic and persisting data to database.
It was created using Spring Boot framework and Java 17. Backend communicates with frontend app using Rest API.
Endpoint definitions (interfaces for Spring controllers) and DTOs are generated by [bs-api-specification](https://github.com/Kaliszando/bs-api-specification) project. 



### Built With

![Java-17][Java]
![Spring][Spring]
![SpringSecurity][SpringSecurity]
![Hibernate][Hibernate]
![JWT][JWT]
![Maven][Maven]

### Additional Dependencies
* Lombok
* JUnit 5, AssertJ, Rest Assured, Mockito
* MapStruct
* Swagger

### Technological Challenges
* JWT authorization
* N+1 problem
* Optimistic locking
* AOP
* Security (password encryption, user context, endpoint authorization)
* Deployment on AWS platform

### UML Diagrams

Vertical cross-section of the ```issue``` domain

![IssueDomain](src/main/resources/static/issue_package.png)

Entity relationship diagram

![ERD](src/main/resources/static/ERD.png)


<p align="right">(<a href="#-bugstalker">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

This is an example of how to list things you need to use the software and how to install them.
* npm
  ```sh
  npm install npm@latest -g
  ```

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/github_username/repo_name.git
   ```
2. Build project
   ```sh
   npm install
   ```
3. Run backend application
   ```js
   const API_KEY = 'ENTER YOUR API';
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

_For more examples, please refer to the [Documentation](https://example.com)_

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ROADMAP -->
## Roadmap

- [ ] Feature 1
- [ ] Feature 2
- [ ] Feature 3
  - [ ] Nested Feature

See the [open issues](https://github.com/github_username/repo_name/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Your Name - [@twitter_handle](https://twitter.com/twitter_handle) - email@email_client.com

Project Link: [https://github.com/github_username/repo_name](https://github.com/github_username/repo_name)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.io/
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[Java]: https://res.cloudinary.com/practicaldev/image/fetch/s--KR6jSVNe--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://img.shields.io/badge/Java-ED8B00%3Fstyle%3Dfor-the-badge%26logo%3Djava%26logoColor%3Dwhite
[Spring]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[SpringSecurity]: https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white
[JWT]: https://img.shields.io/badge/json%20web%20tokens-323330?style=for-the-badge&logo=json-web-tokens&logoColor=pink
[Hibernate]: https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white
[Maven]: https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white
