package project.instagram.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

@MappedSuperclass
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "email"
        })
})
public abstract class User implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(40)")
	@Type(type = "uuid-char")
	private UUID id;
	
	@NotNull
	@Column(name = "email", length = 200, unique = true)
	private String email;
	
	@NotNull
	@Column(name = "password", length = 200)
	private String password;

	@Column(name = "firstName", length = 20)
	private String firstName;

	@Column(name = "lastName", length = 20)
	private String lastName;
	
	@Column(name = "phone", length = 15)
	private String phone;
	
	@Column(name = "address", length = 255)
	private String address;
	
	@Column(name = "createdDate")
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "active")
	private boolean active;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Role role;
	
	
}
