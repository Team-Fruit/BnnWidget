[![Latest Tag](https://img.shields.io/github/tag/Team-Fruit/BnnWidget.svg?label=Latest%20Tag&style=flat)](https://github.com/Team-Fruit/BnnWidget/tags)
[![Latest Release](https://img.shields.io/github/release/Team-Fruit/BnnWidget.svg?label=Latest%20Release&style=flat)](https://github.com/Team-Fruit/BnnWidget/releases)

# BnnWidget
![Minecraft 1.7.10](https://img.shields.io/badge/Minecraft-1.7.10-yellow.svg?style=flat)
![Minecraft 1.8.9](https://img.shields.io/badge/Minecraft-1.8.9-green.svg?style=flat)
![Minecraft 1.9.4](https://img.shields.io/badge/Minecraft-1.9.4-green.svg?style=flat)
![Minecraft 1.10.2](https://img.shields.io/badge/Minecraft-1.10.2-green.svg?style=flat)
![Minecraft 1.11.2](https://img.shields.io/badge/Minecraft-1.11.2-green.svg?style=flat)
![Minecraft 1.12](https://img.shields.io/badge/Minecraft-1.12-green.svg?style=flat)
![Minecraft 1.12.1](https://img.shields.io/badge/Minecraft-1.12.1-green.svg?style=flat)

## Table of Contents

* [About](#about)
* [Contacts](#contacts)
* [License](#license)
* [Downloads](#downloads)
* [Usage](#usage)
* [Example](#example)
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

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.WRenderer;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;

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

## Credits

Thanks to

* Notch et al for Minecraft
* Lex et al for MinecraftForge
* Kamesuta, sjcl for BnnWidget
* all [contributors](https://github.com/Team-Fruit/BnnWidget/graphs/contributors)