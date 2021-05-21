package com.game.entity;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "player")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Player {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//    ID игрока

    @Column(name = "name")
    private String name; // Имя персонажа (до 12 знаков включительно)

    @Column(name = "title")
    private String title;//    Титул персонажа (до 30 знаков включительно)

    @Column(name = "race")
    @Enumerated(EnumType.STRING)
    private Race race; //    Расса персонажа

    @Column(name = "profession")
    @Enumerated(EnumType.STRING)
    private Profession profession; //    Профессия персонажа

    @Column(name = "experience")
    private Integer experience;//    Опыт персонажа. Диапазон значений 0..10,000,000

    @Column(name = "level")
    private Integer level;//    Уровень персонажа

    @Column(name = "untilNextLevel")
    private Integer untilNextLevel; //    Остаток опыта до следующего уровня

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday; //    Дата регистрации Диапазон значений года 2000..3000 включительно

    @Column(name = "banned")
    private Boolean banned; //    Забанен / не забанен

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
        calculations();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }



    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }


    public boolean fillParams(boolean isPartialFill, Player from) {
        String name = from.getName(); // Имя персонажа (до 12 знаков включительно)
        if (name == null) {
            if (!isPartialFill) return false;
            name=this.name;
        } else {
            if (name.length()<1 || name.length()>12) return false;
        };

        String title = from.getTitle();//    Титул персонажа (до 30 знаков включительно
        if (title == null) {
            if (!isPartialFill) return false;
            title=this.title;
        } else {
            if (title.length()>30) return false;
        };

        Race race = from.getRace(); //    Расса персонажа

        if (race == null) {
            if (!isPartialFill) return false;
            race=this.race;
        } else {

        };

        Profession profession = from.getProfession(); //    Профессия персонажа
        if (profession == null) {
            if (!isPartialFill) return false;
            profession=this.profession;
        } else {

        };

        Date birthday = from.getBirthday(); //    Дата регистрации Диапазон значений года 2000..3000 включительно
        if (birthday == null) {
            if (!isPartialFill) return false;
            birthday=this.birthday;
        } else {
            if (birthday.getYear()+1900<2000
                    || birthday.getYear()+1900>3000) return false;
        };

        Boolean banned  = from.getBanned(); //    Забанен / не забанен
        if (banned == null) banned = false;

        Integer experience = from.getExperience();//    Опыт персонажа. Диапазон значений 0..10,000,000
        if (experience == null) {
            if (!isPartialFill) return false;
            experience=this.experience;
        } else {
            if (experience<0 || experience>10000000) return false;
        };

        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
        calculations();

        return true;
    }
    /*public boolean fillParams(boolean isPartialFill, Map<String, String> params) {
        String name = params.get("name"); // Имя персонажа (до 12 знаков включительно)
        if (name == null) {
            if (!isPartialFill) return false;
            name=this.name;
        } else {
            if (name.length()<1 || name.length()>12) return false;
        };

        String title = params.get("title");//    Титул персонажа (до 30 знаков включительно
        if (title == null) {
            if (!isPartialFill) return false;
            title=this.title;
        } else {
            if (title.length()>30) return false;
        };

        Race race; //    Расса персонажа
        String raceValue = params.get("race");
        if (raceValue == null) {
            if (!isPartialFill) return false;
            race=this.race;
        } else {
            try {
                race = Race.valueOf(raceValue);
            } catch (IllegalArgumentException e) {
                return false;
            }
        };

        Profession profession; //    Профессия персонажа
        String profValue = params.get("profession");
        if (profValue == null) {
            if (!isPartialFill) return false;
            profession=this.profession;
        } else {
            try {
                profession = Profession.valueOf(profValue);
            } catch (IllegalArgumentException e) {
                return false;
            }
        };

        Date birthday; //    Дата регистрации Диапазон значений года 2000..3000 включительно
        String dateValue = params.get("date");
        if (dateValue == null) {
            if (!isPartialFill) return false;
            birthday=this.birthday;
        } else {
            try {
                birthday = new Date(Long.valueOf(dateValue));
                if (birthday.getYear()<2000 || birthday.getYear()>3000) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        };

        Boolean banned  = Boolean.valueOf(params.get("banned")); //    Забанен / не забанен

        Integer experience;//    Опыт персонажа. Диапазон значений 0..10,000,000
        String expValue =  params.get("experience");
        if (expValue == null) {
            if (!isPartialFill) return false;
            experience=this.experience;
        } else {
            try {
                experience = Integer.parseInt(expValue);
                if (experience<0 || experience>10000000) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        };

        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
        calculations();

        return true;
    }*/

    private void calculations() {
        level = Double.valueOf((Math.sqrt(2500+200*experience)-50) / 100).intValue();
        untilNextLevel = 50*(level+1)*(level + 2)-experience;
                //50∙(𝑙𝑣𝑙+1)∙(𝑙𝑣𝑙+2)−𝑒𝑥𝑝;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) && Objects.equals(name, player.name) && Objects.equals(title, player.title) && race == player.race && profession == player.profession && Objects.equals(experience, player.experience) && Objects.equals(level, player.level) && Objects.equals(untilNextLevel, player.untilNextLevel) && Objects.equals(birthday, player.birthday) && Objects.equals(banned, player.banned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, race, profession, experience, level, untilNextLevel, birthday, banned);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + birthday +
                ", banned=" + banned +
                '}';
    }
}
