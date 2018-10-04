package org.nick.mics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.nick.mics.domain.enumeration.MicType;

/**
 * A Mic.
 */
@Entity
@Table(name = "mic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Mic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @Column(name = "mic_time")
    private Long micTime;

    @Column(name = "duration")
    private Long duration;

    @NotNull
    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring;

    @Column(name = "recurrence_pattern")
    private String recurrencePattern;

    @NotNull
    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "mic_type")
    private MicType micType;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "mic_host",
               joinColumns = @JoinColumn(name = "mics_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "hosts_id", referencedColumnName = "id"))
    private Set<Host> hosts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Mic name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Mic startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Mic endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getMicTime() {
        return micTime;
    }

    public Mic micTime(Long micTime) {
        this.micTime = micTime;
        return this;
    }

    public void setMicTime(Long micTime) {
        this.micTime = micTime;
    }

    public Long getDuration() {
        return duration;
    }

    public Mic duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Boolean isIsRecurring() {
        return isRecurring;
    }

    public Mic isRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
        return this;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public Mic recurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
        return this;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public Mic streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Mic postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public Mic city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNotes() {
        return notes;
    }

    public Mic notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MicType getMicType() {
        return micType;
    }

    public Mic micType(MicType micType) {
        this.micType = micType;
        return this;
    }

    public void setMicType(MicType micType) {
        this.micType = micType;
    }

    public User getUser() {
        return user;
    }

    public Mic user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Host> getHosts() {
        return hosts;
    }

    public Mic hosts(Set<Host> hosts) {
        this.hosts = hosts;
        return this;
    }

    public Mic addHost(Host host) {
        this.hosts.add(host);
        host.getMics().add(this);
        return this;
    }

    public Mic removeHost(Host host) {
        this.hosts.remove(host);
        host.getMics().remove(this);
        return this;
    }

    public void setHosts(Set<Host> hosts) {
        this.hosts = hosts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mic mic = (Mic) o;
        if (mic.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mic.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Mic{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", micTime=" + getMicTime() +
            ", duration=" + getDuration() +
            ", isRecurring='" + isIsRecurring() + "'" +
            ", recurrencePattern='" + getRecurrencePattern() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", notes='" + getNotes() + "'" +
            ", micType='" + getMicType() + "'" +
            "}";
    }
}
