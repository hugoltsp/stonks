package io.github.hugoltsp.stonks.presenter.telegram.bot

import io.github.hugoltsp.stonks.infra.domain.Settings
import io.github.hugoltsp.stonks.infra.extensions.getLogger
import io.github.hugoltsp.stonks.usecase.AddStockToSubscriber
import io.github.hugoltsp.stonks.usecase.RemoveSubscription
import io.github.hugoltsp.stonks.usecase.RetrieveStockByIdentifier
import io.github.hugoltsp.stonks.usecase.RetrieveSubscriptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.command
import me.ivmg.telegram.dispatcher.telegramError
import me.ivmg.telegram.entities.ParseMode
import me.ivmg.telegram.entities.Update
import java.time.Duration

class StonksBot(
    private val addStockToSubscriber: AddStockToSubscriber = AddStockToSubscriber(),
    private val retrieveStockByIdentifier: RetrieveStockByIdentifier = RetrieveStockByIdentifier(),
    private val retrieveSubscriptions: RetrieveSubscriptions = RetrieveSubscriptions(),
    private val removeSubscription: RemoveSubscription = RemoveSubscription()
) {

    fun run() {
        startCommandListener()
        startNotificationRoutine()
    }

    private fun startCommandListener() = bot {
        token = Settings.telegramToken
        dispatch {
            command(ADD_COMMAND) { _, update, args ->
                try {
                    if (args.isNotEmpty() && update.hasUser()) {
                        update.message?.from?.id?.let {
                            addStockToSubscriber.add(it, args.first().split(","))
                        }
                    }
                } catch (e: Exception) {
                    logger.error("$ADD_COMMAND Failed.", e)
                }
            }

            command(PEEK_COMMAND) { bot, update, args ->
                try {
                    if (args.isNotEmpty() && update.hasUser()) {
                        update.message?.from?.id?.let {
                            retrieveStockByIdentifier.retrieve(args.first())?.run {
                                bot.sendMessage(it, toString(), ParseMode.MARKDOWN)
                            }
                        }
                    }
                } catch (e: Exception) {
                    logger.error("$PEEK_COMMAND Failed.", e)
                }
            }

            command(UNSUBSCRIBE_COMMAND) { bot, update, _ ->
                try {
                    if (update.hasUser()) {
                        update.message?.from?.id?.let {
                            removeSubscription.unsubscribe(it)
                            bot.sendMessage(it, UNSUBSCRIBE_MESSAGE)
                        }
                    }
                } catch (e: Exception) {
                    logger.error("$PEEK_COMMAND Failed.", e)
                }
            }

            command(START_COMMAND) { bot, update, _ ->
                if (update.hasUser()) {
                    update.message?.from?.id?.let {
                        bot.sendMessage(
                            it, START_MESSAGE_TEMPLATE, ParseMode.MARKDOWN
                        )
                    }
                }
            }

            telegramError { _, error ->
                logger.error("${error.getType()} - ${error.getErrorMessage()}")
            }
        }
    }.startPolling()


    private fun startNotificationRoutine() {
        val bot = bot {
            token = Settings.telegramToken
        }
        GlobalScope.launch {
            while (true) {
                try {
                    retrieveSubscriptions
                        .retrieveAll()
                        .groupBy { it.subscriberId }
                        .forEach { (subscriberId, stocks) ->
                            logger.info("Notifying user [{}]", subscriberId)

                            bot.sendMessage(subscriberId, stocks.asSequence()
                                .map { it.stockName }
                                .map { retrieveStockByIdentifier.retrieve(it) }
                                .filterNotNull()
                                .map { it.toString() }
                                .joinToString("\n\n"), ParseMode.MARKDOWN)

                        }
                    delay(Duration.ofMinutes(Settings.notificationScheduleMinutes).toMillis())
                } catch (e: Exception) {
                    logger.error("Error.", e)
                }
            }
        }
    }

    private fun Update.hasUser() = message != null && message!!.from != null

    private companion object {

        const val ADD_COMMAND = "add"
        const val START_COMMAND = "start"
        const val PEEK_COMMAND = "ver"
        const val UNSUBSCRIBE_COMMAND = "flw"

        const val UNSUBSCRIBE_MESSAGE = "Ate mais, espero que possamos voltar a ser amigos novamente algum dia!"
        const val START_MESSAGE_TEMPLATE =
            "***/add itub3,xpml11*** - adiciona acoes para voce ser notificado\n\n***/ver mglu3*** - faz consulta\n\n***/flw*** - cancela as notificacoes!"

        val logger = getLogger()
    }

}
