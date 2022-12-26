### sajooq --help
Displays list of commands and their descriptions

## Generate Changelog from existing database:
```bash
sajooq changelog generate --db-name {dbName} --db-user {dbUser} --db-password {dbPassword}
```
* Check if it is possible to generate changelog per entity (improves readability and eases tracking specific entities state on changelog)

## Generate entities from changelog:
```bash
sajooq entity generate --changelog {changelog}
```
* Check if it is possible to generate records entities

## Update changelog after entity modification
```bash
sajooq changelog update --entity {entity}
```

## Update database after changlog modification
```bash
sajooq database update --changelog {entity}
```

## Update database after entity modification
```bash
sajooq database update --entity {entity}
```
Which is done by:
1. sajooq changelog update --entity {entity}
2. sajooq database update --changelog {changelog}



