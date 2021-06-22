package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = ALL)
    private List<Section> sections = new ArrayList<>();

    protected Line() { }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line create(String name, String color) {
        return new Line(name, color);
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String color() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section getUpStation() {
        for (Section section: sections) {
            if(section.getUpStation() != null) return section;
        }
        throw new IllegalStateException("상행선 종점 정보가 없는 잘못된 노선 정보입닌다.");
    }

    public Section getDownStation() {
        for (Section section: sections) {
            if(section.getDownStation() != null) return section;
        }
        throw new IllegalStateException("하행선 종점 정보가 없는 잘못된 노선 정보입닌다.");
    }

    public Line setSection(Section station) {
        this.sections.add(station);
        station.setLine(this);
        return this;
    }
}
