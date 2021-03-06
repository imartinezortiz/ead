/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2014 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          CL Profesor Jose Garcia Santesmases 9,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.ead.editor.view.widgets.mockup.panels.behaviours;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import es.eucm.ead.engine.I18N;
import es.eucm.ead.schema.components.behaviors.timers.Timer;
import es.eucm.ead.schema.components.behaviors.touches.Touch;
import es.eucm.ead.schema.data.Condition;
import es.eucm.ead.schema.effects.Effect;
import es.eucm.ead.schema.effects.GoTo;

public class GoToPanel extends EffectBehaviourPanel {

	private TextField valueX;

	private TextField valueY;

	public GoToPanel(Skin skin, I18N i18n) {
		super(skin);

		this.valueX = new TextField("0.0", skin);
		this.valueY = new TextField("0.0", skin);

		this.add(new Label("X : ", skin));
		this.add(this.valueX).expandX().fill();
		this.row();
		this.add(new Label("Y : ", skin));
		this.add(this.valueY).expandX().fill();
	}

	@Override
	public void actBehaviour(Condition c) {
		Effect effect = new GoTo();
		((GoTo) effect).setX(Float.valueOf(valueX.getText()));
		((GoTo) effect).setY(Float.valueOf(valueY.getText()));
		if (c instanceof Timer) {
			((Timer) c).getEffects().set(0, effect);
		} else {
			((Touch) c).getEffects().set(0, effect);
		}
	}

	public void actPanel(float x, float y) {
		this.valueX.setText("" + x);
		this.valueY.setText("" + y);
	}
}