package proglife.fora.bank.features.chat

import io.reactivex.Single
import proglife.fora.bank.features.chat.models.Chat

/**
 * Created by Evhenyi Shcherbyna on 25.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface ChatRepository {
    fun getChats(): Single<List<Chat>>
}

class ChatRepositoryTest: ChatRepository {

    override fun getChats(): Single<List<Chat>> {
        return Single.fromCallable {
            listOf(
                    Chat(
                            "Павел", "Герасимчук",
                            "https://media.nngroup.com/media/people/photos/IMG_2366-copy-400x400.jpg.400x400_q95_autocrop_crop_upscale.jpg",
                            "14:30", "Вы: Все понятно.", 0),
                    Chat(
                            "Игорь", "Констанинов",
                            "https://1ofdmq2n8tc36m6i46scovo2e-wpengine.netdna-ssl.com/wp-content/uploads/2016/11/Tobias-Erb_MPI-Marburg.jpg",
                            "12:24", "Да, отлично.", 2),
                    Chat(
                            "Инна", "Сидорова",
                            "https://relayfm.s3.amazonaws.com/uploads/user/avatar/66/user_avatar_katiefloyd_artwork.png",
                            "10:45", "Вы: Деньги получила.", 0),
                    Chat(
                            "Иван", "Панов",
                            "https://fedspendingtransparency.github.io/assets/img/user_personas/repurposer_mug.jpg",
                            "00:12", "Да, достаточно", 0),
                    Chat(
                            "Иван", "Петров",
                            "https://developers.google.com/web/images/contributors/philipwalton.jpg",
                            "00:02", "Вы: А сколько вышло?.", 0),
                    Chat(
                            "Владислав", "Пасечник",
                            "http://www.indiewire.com/wp-content/uploads/2018/04/gotti-john-travolta1.jpg?w=780",
                            "00:00", "Вы: Все понятно.", 0)
            )
        }
    }

}