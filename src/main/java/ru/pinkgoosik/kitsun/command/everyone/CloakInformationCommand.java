package ru.pinkgoosik.kitsun.command.everyone;

import discord4j.core.object.entity.Member;
import discord4j.rest.entity.RestChannel;
import ru.pinkgoosik.kitsun.command.Command;
import ru.pinkgoosik.kitsun.command.CommandUseContext;
import ru.pinkgoosik.kitsun.config.Config;
import ru.pinkgoosik.kitsun.cosmetica.PlayerCloaks;
import ru.pinkgoosik.kitsun.perms.AccessManager;
import ru.pinkgoosik.kitsun.perms.Permissions;

import java.util.ArrayList;

import static ru.pinkgoosik.kitsun.command.everyone.CloakGrantCommand.PREVIEW_CLOAK;

public class CloakInformationCommand extends Command {

    @Override
    public String getName() {
        return "information cloak";
    }

    @Override
    public String getDescription() {
        return "Tells the player information about their current cloak";
    }

    @Override
    public String appendName() {
        return "**" + Config.general.prefix + this.getName() + "**";
    }

    @Override
    public void respond(CommandUseContext context) {
        RestChannel channel = context.getChannel();
        Member member = context.getMember();
        String discordId = member.getId().asString();
        String username = getNicknames(discordId).get(0);

        if (!AccessManager.hasAccessTo(member, Permissions.CLOAK_INFORMATION)) {
            channel.createMessage(createErrorEmbed("Not enough permissions for this command.")).block();
            return;
        }

        for (PlayerCloaks.Entry entry : PlayerCloaks.ENTRIES) {
            if (entry.user.name.equals(username)) {
                String text = "Your current cape is " + entry.cloak.name + ".";
                channel.createMessage(createSuccessfulEmbed("Information", text, PREVIEW_CLOAK.replace("%cloak%", entry.cloak.name))).block();
            }
        }
    }

    private static ArrayList<String> getNicknames(String discord){
        ArrayList<String> nicknames = new ArrayList<>();
        for(var entry : PlayerCloaks.ENTRIES){
            if(entry.user.discord.equals(discord)) nicknames.add(entry.user.name);
        }
        return nicknames;
    }
}