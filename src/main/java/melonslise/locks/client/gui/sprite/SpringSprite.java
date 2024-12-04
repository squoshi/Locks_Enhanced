package melonslise.locks.client.gui.sprite;

import com.mojang.blaze3d.vertex.PoseStack;
import melonslise.locks.client.util.LocksClientUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpringSprite extends Sprite
{
	public final TextureInfo[] texs;
	public Sprite target;
	public ResourceLocation resourceLocation;

	public SpringSprite(TextureInfo[] texs, Sprite target)
	{
		super(texs[0]);
		this.texs = texs;
		this.target = target;
	}

	@Override
	public void draw(GuiGraphics mtx, float partialTick, ResourceLocation location)
	{
		for(TextureInfo tex : this.texs)
			if(LocksClientUtil.lerp(this.target.oldPosY, this.target.posY, partialTick) < this.posY + tex.height)
				this.tex = tex;
		super.draw(mtx, partialTick, location);
	}
}