# mchr

MCHR (mc head render) is a simple Minecraft skin rendering service. MCHR can be configured to use a variety of skin providers. While other services
such as Minotar and Crafatar only allow you to render the skin of usernames / uuids, MCHR allows you to render skins that are not in use by players.

Public instance: mchr.cerus.dev

## Usage

Example usage:

Render a flat head using a Mojang skin\
`https://mchr.cerus.dev/v1/render/74d1e08b0bb7e9f590af27758125bbed1778ac6cef729aedfcb9613e9911ae75`

Render an isometric head using a Mojang skin\
`https://mchr.cerus.dev/v1/render/74d1e08b0bb7e9f590af27758125bbed1778ac6cef729aedfcb9613e9911ae75?renderer=isometric`

Render an isometric head using a Mojang skin of size 256x256\
`https://mchr.cerus.dev/v1/render/74d1e08b0bb7e9f590af27758125bbed1778ac6cef729aedfcb9613e9911ae75?renderer=isometric&size=256`

Render a flat head using a Crafatar skin of size 128x128 without overlay\
`https://mchr.cerus.dev/v1/render/Cerus_?skin=crafatar&size=128&overlay=false`

## Installation

### Using docker-compose

Create a `.env` file with the following contents (change the values if needed):

```
MCHR_HOST="0.0.0.0"
MCHR_PORT="8080"
MCHR_IMAGE_CACHE_EXPIRATION="300"
MCHR_MAX_REQUESTS="120"
MCHR_RATE_LIMITER_EXPIRATION="60"
```

Create a `docker-compose.yml` file with the following contents:

```yaml
services:
  mchr:
    container_name: "mchr"
    restart: always
    image: "openjdk:17"
    ports:
      - "8099:8080" # Change 8099 to the port you want this to be exposed on
    volumes:
      - ./data:/opt/data
    command: "bash -c \"cd /opt/data && java -jar mc-head-render-*.jar\""
    environment:
      MCHR_IMAGE_CACHE_EXPIRATION: "${MCHR_IMAGE_CACHE_EXPIRATION}"
      MCHR_MAX_REQUESTS: "${MCHR_MAX_REQUESTS}"
      MCHR_RATE_LIMITER_EXPIRATION: "${MCHR_RATE_LIMITER_EXPIRATION}"
      MCHR_HOST: "${MCHR_HOST}"
      MCHR_PORT: "${MCHR_PORT}"
```

After this run `docker-compose up -d` to start the container.

## Configuration

### Environment variables

MCHR_HOST: The web server host\
MCHR_PORT: The web server port\
MCHR_IMAGE_CACHE_EXPIRATION: The expiration of cached skins in seconds\
MCHR_MAX_REQUESTS: Maximum allowed request in the configured amount of time\
MCHR_RATE_LIMITER_EXPIRATION: The rate limit period in seconds

### Skin providers

Skin providers can be configured using the `skin_providers.json` file. Example configuration:

```json5
{
  "skin_provider": {
    "namemc": {
      // Get NameMC skin by id
      "url": "https://s.namemc.com/i/%s.png",
      "pattern": "[\\da-f]{16}"
    },
    "crafatar": {
      // UUID to skin
      "url": "https://crafatar.com/skins/%s",
      "pattern": "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"
    },
    "minotar": {
      // Username / UUID to skin
      "url": "https://minotar.net/skin/%s",
      "pattern": "(([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})|([a-zA-Z0-9_]{3,16}))"
    }
  }
}
```

## Building from source

You need:

- Java 17
- Maven
- Git

1. Clone the repository (`git clone https://github.com/cerus/mchr`)
2. Cd into the cloned repo (`cd mchr`)
3. Build the project (`mvn clean package`)
4. The build artifact can be found in `target/` (`mc-head-render-VERSION.jar`)

## Reporting issues

Please [open a new issue](https://github.com/cerus/mchr/issues/new) to report bugs / request features etc.

## Contributing

Please take a look at the [CONTRIBUTING](CONTRIBUTING.md) file.

## Buy me a coffee

[:heart: Sponsor me on GitHub](https://github.com/sponsors/cerus)