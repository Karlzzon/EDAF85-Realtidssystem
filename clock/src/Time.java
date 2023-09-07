public class Time {
    private int hours;
    private int minutes;
    private int seconds;

    boolean alarmON = false;

    private int alarmHours;
    private int alarmMinutes;
    private int alarmSeconds;

    public Time() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.alarmHours = 0;
        this.alarmMinutes = 0;
        this.alarmSeconds = 0;
    }

    public int getHours() {
        return this.hours;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setAlarmHours(int hours) {
        this.alarmHours = hours;
    }

    public void setAlarmMinutes(int minutes) {
        this.alarmMinutes = minutes;
    }

    public void setAlarmSeconds(int seconds) {
        this.alarmSeconds = seconds;
    }

    public int getAlarmHours() {
        return this.alarmHours;
    }

    public int getAlarmMinutes() {
        return this.alarmMinutes;
    }

    public int getAlarmSeconds() {
        return this.alarmSeconds;
    }

    public boolean match() {
        if (this.hours == this.alarmHours &&
                this.minutes == this.alarmMinutes &&
                this.seconds == this.alarmSeconds &&
                this.alarmON) {
            System.out.println("match");
            return true;
        }
        return false;
    }

    public boolean alarmToggle() {
        this.alarmON = !this.alarmON;
        return this.alarmON;
    }

    public void tick() {
        this.seconds++;
        if (this.seconds == 60) {
            this.seconds = 0;
            this.minutes++;
            if (this.minutes == 60) {
                this.minutes = 0;
                this.hours++;
                if (this.hours == 24) {
                    this.hours = 0;
                }
            }
        }
    }

}
