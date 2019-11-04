package projects.csce.evence.service.model.qr;


import java.util.Objects;


public class Qr
{
    private String title;
    private String startDate;
    private String endDate;
    private String location;
    private String description;

    public static class Builder
    {
        private String title;
        private String startDate;
        private String endDate;
        private String location;
        private String description;


        public Builder title(String title)
        {
            Objects.requireNonNull(title, "title == null");
            this.title = title;
            return this;
        }

        public Builder startDate(String startDate)
        {
            Objects.requireNonNull(startDate, "startDate == null");
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(String endDate)
        {
            this.endDate = endDate;
            return this;
        }

        public Builder location(String location)
        {
            this.location = location;
            return this;
        }

        public Builder description(String description)
        {
            this.description = description;
            return this;
        }

        public Qr build()
        {
            return new Qr(this);
        }
    }

    private Qr(Builder builder)
    {
        title = builder.title;
        startDate = builder.startDate;
        endDate = builder.endDate;
        location = builder.location;
        description = builder.description;
    }

    String text()
    {
        return "BEGIN:VEVENT" +
            "\nSUMMARY:" + title +
            "\nDTSTART:" + startDate +
            "\nDTEND:" + endDate +
            "\nLOCATION:" + location +
            "\nDESCRIPTION:" + description +
            "\nEND:VEVENT\n";
    }

}
