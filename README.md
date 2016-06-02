# rest-async

Spawn async REST calls and collect the results.
Use Spring-Reactor and experiment with two types of Dispatchers
`Threadpool` and `RingBuffer`

Reactor makes it seamless and convenient to handle asynchronous processing.
We do not have to think about managing `Future` and spawning tasks. It's handled by the *event-bus*

It's easy to implement and easy to maintain and understand.

Additionally, it truly gives the opportunity to operate on individual request as and when it gets completed.
This gives us an option to take the process to its completion for one single granular request, rather than having to wait for it.
