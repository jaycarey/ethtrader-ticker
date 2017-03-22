# ethtrader-ticker

Creates a 'ticker' by pulling in various pieces of data and uploads it to reddit.

# Configuration

See the 'ticker.conf' https://github.com/jaycarey/ethtrader-ticker-conf at for inspiration.

Structure:
- `ticker.conf` - the main configuration file for the ticker. This file is quite elaborate to try and make it as flexible as possible, it's probably best to learn by example from the existing configuration above.
- `ticker.css` - CSS that applies to the ticker as a whole.
- `per-ticker.css` - tickers have an 'upper' and 'lower' portion - this css is include for each one with certain placeholders applied.
- `img/` - these are the images referred to in 'ticker.conf'

# Build

```
mvn clean package
```

# Deploy

Unzip the zip at `target/ethtrader-ticker-zip.zip` to the desired location.

# Run

An 'application' must be created. See here while logged in as the bot: https://www.reddit.com/prefs/apps/

```
bash bin/start.sh <<subreddit>> <<username>> <<password>> <<clientId>> <<secret>> <<resourceUrl>>
```

*subreddit* - is the name of the subreddit to show the ticker on.
*username* - is the name of the bot's (must have the ability to modify CSS on the reddit.)
*password* - password of the bot.
*clientId* - the clientId from the application created above.
*secret* - the secret from the application created above.
*resourceUrl* - The base of the url the ticker's configuration is located at, e.g. for the configuration above: https://raw.githubusercontent.com/jaycarey/ethtrader-ticker-conf/master/ would be the base.

