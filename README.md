[![Latest Tag](https://img.shields.io/github/tag/Team-Fruit/BnnWidget.svg?label=Latest%20Tag&style=flat)](https://github.com/Team-Fruit/BnnWidget/tags)
[![Latest Release](https://img.shields.io/github/release/Team-Fruit/BnnWidget.svg?label=Latest%20Release&style=flat)](https://github.com/Team-Fruit/BnnWidget/releases)

# BnnWidget
![Minecraft 1.7.10](https://img.shields.io/badge/Minecraft-1.7.10-yellow.svg?style=flat)
![Minecraft 1.8.9](https://img.shields.io/badge/Minecraft-1.8.9-green.svg?style=flat)
![Minecraft 1.9.4](https://img.shields.io/badge/Minecraft-1.9.4-green.svg?style=flat)
![Minecraft 1.10.2](https://img.shields.io/badge/Minecraft-1.10.2-green.svg?style=flat)
![Minecraft 1.11](https://img.shields.io/badge/Minecraft-1.11-green.svg?style=flat)
![Minecraft 1.11.2](https://img.shields.io/badge/Minecraft-1.11.2-green.svg?style=flat)

## Table of Contents

* [About](#about)
* [Contacts](#contacts)
* [License](#license)
* [Downloads](#downloads)
* [Usage](#usage)
* [Example](#example)
* [Issues](#issues)
* [Building](#building)
* [Contribution](#contribution)
* [Localization](#bnnwidget-localization)
* [Credits](#credits)

## About

Easy but powerful Minecraft GUI widget

## Contacts

* [GitHub](https://github.com/Team-Fruit/BnnWidget)

## License

* Banana Widget
  - (c) 2016 TeamFruit
  - [![License](https://img.shields.io/badge/license-MIT-blue.svg?style=flat)](https://opensource.org/licenses/mit-license.php)
* Textures and Models
  - (c) 2016 TeamFruit
  - [![License](https://img.shields.io/badge/License-CC%20BY--NC--SA%203.0-yellow.svg?style=flat)](https://creativecommons.org/licenses/by-nc-sa/3.0/)
* Text and Translations
  - [![License](https://img.shields.io/badge/License-No%20Restriction-green.svg?style=flat)](https://creativecommons.org/publicdomain/zero/1.0/)

## Downloads

Downloads can be found on [github](https://github.com/Team-Fruit/BnnWidget/releases).

## Usage

Change the package name to anything you want and include these files!

## Example

```java
import javax.annotation.Nonnull;

import net.teamfruit.bnnwidget.WBase;
import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.WFrame;
import net.teamfruit.bnnwidget.WPanel;
import net.teamfruit.bnnwidget.WRenderer;
import net.teamfruit.bnnwidget.motion.Easings;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.render.OpenGL;
import net.teamfruit.bnnwidget.var.V;
import net.teamfruit.bnnwidget.var.VMotion;

// your main gui extends "WFrame"
public class MyGui extends WFrame {
	// init widgets in "initWidget"
	@Override
	protected void initWidget() {
		// add your panel extends "WPanel"
		add(new WPanel(new R()) {
			@Override
			protected void initWidget() {
				// "R" is a class that relatively expresses an area.
				add(new WBase(new R()) {
					// "VMotion" is a constantly changing value depending on the specified animation.
					VMotion m = V.pm(0);

					@Override
					public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float opacity) {
						// Let's prepare before rendering with "WRenderer.startShape()"
						WRenderer.startShape();
						// BnnWidget provides a GL wrapper that absorbs differences in Minecraft versions.
						// The value of the "V" class is retrieved by get () every time.
						OpenGL.glColor4f(0f, 0f, 0f, this.m.get());
						// "WGui" has drawing methods that can be specified in detail with a float value.
						draw(getGuiPosition(pgp));
					}

					private boolean mouseinside;

					@Override
					public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
						// The area of the parent widget becomes get Area of the child widget by getGuiPosition.
						final Area a = getGuiPosition(pgp);
						// PointInside determines if the cursor is over the widget
						if (a.pointInside(p)) {
							if (!this.mouseinside) {
								this.mouseinside = true;
								// Add animation and start it. Release the queue with stop.
								this.m.stop().add(Easings.easeLinear.move(.2f, 0f)).start();
							}
						} else if (this.mouseinside) {
							this.mouseinside = false;
							this.m.stop().add(Easings.easeLinear.move(.2f, .5f)).start();
						}
						super.update(ev, pgp, p);
					}

					// It is called when the GUI is closed. If you want to add closing animation, return false and let's wait.
					@Override
					public boolean onCloseRequest() {
						this.m.stop().add(Easings.easeLinear.move(.25f, 0f)).start();
						return false;
					}

					// The GUI will not exit until it returns true. Let's see if the animation has ended with isFinished.
					@Override
					public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse) {
						return this.m.isFinished();
					}
				});
			}
		});
	}
}
```

## Issues

BnnWidget crashing, have a suggestion, found a bug?  Create an issue now!

1. Make sure your issue has not already been answered or fixed and you are using the latest version. Also think about whether your issue is a valid one before submitting it.
2. Go to [the issues page](https://github.com/Team-Fruit/BnnWidget/issues) and click [new issue](https://github.com/Team-Fruit/BnnWidget/issues/new)
3. Enter your a title of your issue (something that summarizes your issue), and then create a detailed description of the issue.
    * The following details are required. Not including them can cause the issue to be closed.
        * Forge version
        * BnnWidget version
        * Crash log, when reporting a crash (Please make sure to use [pastebin](http://pastebin.com/))
            * Do not post an excerpt of what you consider important, instead:
            * Post the full log
        * Other mods and their version, when reporting an issue between BnnWidget and another mod
            * Also consider updating these before submitting a new issue, it might be already fixed
        * A detailed description of the bug or feature
    * To further help in resolving your issues please try to include the follow if applicable:
        * What was expected?
        * How to reproduce the problem?
            * This is usually a great detail and allows us to fix it way faster
        * Screen shots or Pictures of the problem
5. Click `Submit New Issue`, and wait for feedback!

Providing as many details as possible does help us to find and resolve the issue faster and also you getting a fixed version as fast as possible.

## Building

1. Clone this repository via
  - SSH `git clone git@github.com:Team-Fruit/BnnWidget.git` or
  - HTTPS `git clone https://github.com/Team-Fruit/BnnWidget.git`
2. Setup workspace
  - Decompiled source `gradlew setupDecompWorkspace`
  - Obfuscated source `gradlew setupDevWorkspace`
  - CI server `gradlew setupCIWorkspace`
3. Build `gradlew build`. Jar will be in `build/libs`
4. For core developer: Setup IDE
  - IntelliJ: Import into IDE and execute `gradlew genIntellijRuns` afterwards
  - Eclipse: execute `gradlew eclipse`

## Contribution

Before you want to add major changes, you might want to discuss them with us first, before wasting your time.
If you are still willing to contribute to this project, you can contribute via [Pull-Request](https://help.github.com/articles/creating-a-pull-request).

The [guidelines for contributing](https://github.com/Team-Fruit/BnnWidget/blob/master/CONTRIBUTING.md) contain more detailed information about topics like the used code style and should also be considered.

Here are a few things to keep in mind that will help get your PR approved.

* A PR should be focused on content.
* Use the file you are editing as a style guide.
* Consider your feature.
  - Is your suggestion already possible using Vanilla + Sign Picture?
  - Make sure your feature isn't already in the works, or hasn't been rejected previously.
  - Does your feature simplify another feature of Sing Picture? These changes will not be accepted.
  - If your feature can be done by any popular mod, discuss with us first.

Getting Started

1. Fork this repository
2. Clone the fork via
  * SSH `git clone git@github.com:<your username>/BnnWidget.git` or
  * HTTPS `git clone https://github.com/<your username>/BnnWidget.git`
3. Change code base
4. Add changes to git `git add -A`
5. Commit changes to your clone `git commit -m "<summery of made changes>"`
6. Push to your fork `git push`
7. Create a Pull-Request on GitHub
8. Wait for review
9. Squash commits for cleaner history

If you are only doing single file pull requests, GitHub supports using a quick way without the need of cloning your fork. Also read up about [synching](https://help.github.com/articles/syncing-a-fork) if you plan to contribute on regular basis.

## BnnWidget Localization

### English Text

`en_US` is included in this repository, fixes to typos are welcome.

### Encoding

Files must be encoded as UTF-8.

### New Translations

You can provide any additional languages by creating a new file with the [appropriate language code](http://download1.parallels.com/SiteBuilder/Windows/docs/3.2/en_US/sitebulder-3.2-win-sdk-localization-pack-creation-guide/30801.htm).

## Credits

Thanks to

* Notch et al for Minecraft
* Lex et al for MinecraftForge
* Kamesuta, sjcl for BnnWidget
* all [contributors](https://github.com/Team-Fruit/BnnWidget/graphs/contributors)
