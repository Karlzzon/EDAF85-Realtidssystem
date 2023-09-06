1. The main method (sketched above) handles user input. What additional thread(s) do you need, beyond
this main thread?

Vi behöver en tråd som tickar tiden (main), en tråd som hanterar input från användaren, en tråd som hanterar alarm. (SRP - en tråd för alarmTid och en för AlarmBeeps)

2. What common data needs to be shared between threads? Where is the data to be stored?
Hint: introduce a dedicated class for this shared data, as outlined above

Tiden (alltså vad klockan är), userInputs (setLarm, setTime etc)

3. For each of your threads, consider:
• What operations on shared data are needed for the thread?
• Where in the code is this logic best implemented?

Om vår input är en setTime så måste en tid skickas med från inputten till mainTråden för att uppdatera klockans currentTime. Om det är ett setAlarm behöver alarmTid tråden få klockansTid så den kan undersöka om det matchar och när det matchar typ sätta 1 på en semafor som drar igång beepsen.

Logiken är nog bäst implementerad i trådarna.

4. . In which parts of your code is data accessed concurrently from different threads? Where in your code
do you need to ensure mutual exclusion?

Vi behöver mutual exclusion på mainTråden som uppdaterar tiden och när man uppdaterar tiden genom userInputs.
Vi har även en spontan tanke om att man kan låta alarmTicks tråden uppdatera klockans värde sålänge det finns ett alarm, vilket hade inneburit att vi behöver mutual exclusion där också.

5.Are there other situations in the alarm clock where semaphores are to be used?
Hint: have a look at ClockInput in section 1.1.2.

Vi behöver en semaphore för att registrera att vi fått en input och att vi behöver sätta ett alarm/uppdatera tiden. 

Vi behöver en mellan alarmTicks och alarmBeeps för att visa börja blinka när alarmet går igång.

