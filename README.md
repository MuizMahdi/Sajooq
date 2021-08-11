# Sajooq
A library and a CLI for simplifying development, and managing entities lifecycles using Spring, Liquibase, Hibernate, and JOOQ.

<br>

## Features
- A generic resource repository that implements the most frequently used queries, utilizing JOOQ and Spring Webflux
- A CLI for managing JOOQ records life cycle

<br>

## Setup
1. Add Sajooq to your pom.xml

```xml
<dependency>
    <groupId>io.github.muizmahdi</groupId>
    <artifactId>sajooq</artifactId>
    <version>1.0.4</version>
</dependency>
```

2. Make your Entity, POJO, or DAO extend `BaseEntity`:
```java

@Data
public class Account extends BaseEntity {
    private String username, email, password;
}

```

3. Create an interface for your resource repository that extends `ResourceRepository`:
```java

@Repository
public interface AccountRepository extends ResourceRepository<Account, AccountsRecord, Accounts> {
    // Account          => POJO
    // AccountsRecord   => JOOQ generated record
    // Accounts         => JOOQ generated TableImpl of the record
}

```
4. Create an implementation for that interface, which extends `DefaultResourceRepository`:

```java

@Repository
public class AccountRepositoryImpl extends DefaultResourceRepository<Account, AccountsRecord, Accounts>
implements AccountRepository {

}

```
5. Inject your repository and use any of the queries
```java

@Autowired private AccountRepository accountRepository;

@Override
public Mono<ApiResponse> create(Account accountToCreate) {

    // Insert Account
    return accountRepository.insert(accountToCreate, Accounts.ACCOUNTS).flatMap(account -> {
        return Mono.just(new ApiResponse(account, HttpStatus.CREATED.value()));
    });

}

```
