# TouchtunesSampleApp

Hello guys,

Here is an assessment I completed 2 years ago for the company where I am currently employed.

Basically, I upgraded Gradle to be able to run the project and made adjustments in project
structure, cleaned up the UI, and reduced complexity in the ViewModel
to better align with my current technical standards.

I didn't change the libs I used when I built the project and the logic neither.
So here is my self review and the improvement I could have bring today :

- Use Hilt for dependency injection instead of Koin for better integration with the Android
  ecosystem.
- Replace Retrofit with Ktor since Ktor is built in Kotlin and leverages Coroutines, offering more
  cohesive code with Kotlin projects. And use Result<> to handle success failures
- While I used a State Machine pattern inspired by Redux which is suitable for small projects or
  assessments, I would be in favor of building a State Machine SDL inspired by this
  project: [Tinder/StateMachine](https://github.com/Tinder/StateMachine).
- Avoid using Dimensions to store dp values; instead, create Objects and build a Design System.
- Put more effort into the UI since it is currently quite basic.
- Use the edgeToEdge


That said, good review!