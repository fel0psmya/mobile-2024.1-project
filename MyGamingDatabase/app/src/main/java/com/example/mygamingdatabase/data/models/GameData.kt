package com.example.mygamingdatabase.data.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.mygamingdatabase.R

data class Game(
    val id: Int,  // Unique ID from the game
    val name: String?,  // Game name
    val imageUrl: String?,  // Game cover URL
    val trailerUrl: String?, // Trailer URL
    val description: String?,  // Game description
    val platforms: List<String>?,  // Game platforms
    val releaseDate: String?,  // Release year
    val screenshots: List<String>?,  // Screenshots URLs
    val isFavorite: MutableState<Boolean>, // If the game is favorite
    val isAddedToList: MutableState<Boolean>,
    val isReminded: MutableState<Boolean>,
    val reminderTime: MutableState<String> = mutableStateOf(""), // HH:mm format
    val reminderDays: MutableState<String> = mutableStateOf(""), // CSV format: "Monday,Wednesday,Friday"
    var userScore: Int?, // User score (1-10)
    var status: GameStatus, // Status of the game
    var artworkUrl: String?

    /* val genres: List<Genre>,  // Game genres

    val videoUrl: String?,  // Video URL (optional)

    val developers: List<Developer>?,  // Game developers
    val publishers: List<Publisher>?,  // Game publishers
    val similar_games: List<Int>?, // Similar games
    val rating: Double?,  // IGDB game note

     */
) {
    // No-argument constructor for Firebase
    constructor() : this(
        id = 0,
        name = null,
        imageUrl = null,
        trailerUrl = null,
        description = null,
        platforms = null,
        releaseDate = null,
        screenshots = null,
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        reminderTime = mutableStateOf(""),
        reminderDays = mutableStateOf(""),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = null
    )
}

enum class GameStatus {
    PLANNING_TO_PLAY, // Pretendo Jogar
    PLAYING,          // Jogando
    DROPPED,          // Dropado
    COMPLETED         // Completo
}

val statusDescriptions = mapOf(
    GameStatus.PLANNING_TO_PLAY to "Pretendo Jogar",
    GameStatus.PLAYING to "Jogando",
    GameStatus.DROPPED to "Dropado",
    GameStatus.COMPLETED to "Completo"
)

val scoreDescriptions = mapOf(
    0 to "Sem comentários",
    1 to "Caquinha",
    2 to "Muito ruim",
    3 to "Ruim",
    4 to "Mediano",
    5 to "Ok",
    6 to "Definitivamente um dos jogos já feitos",
    7 to "Bom",
    8 to "Ótimo",
    9 to "Excelente",
    10 to "Obra-prima"
)

data class ReleaseDate(
    val date: Long  // Release date timestamp
)

/*
data class Genre(
    val name: String  // Genre name
)

data class Platform(
    val name: String  // Platform name
)

data class Developer(
    val name: String  // Dev name
)

data class Publisher(
    val name: String  Publisher name
)*/

val gameList = listOf(
    Game (
        id = 1,
        name = "Enigma do Medo",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co93ft.webp",
        trailerUrl = "https://www.youtube.com/watch?v=Hjl6usm5WCo",
        description = "Become Mia, a paranormal detective searching for her missing father inside the Perimeter - a place that doesn't exist. Investigate clues like a real detective and unravel the mysteries behind the Enigma of Fear, defeating the terrible monsters who'll try to stop you.",
        platforms = listOf("PC (Microsoft Windows)"),
        releaseDate = "2024",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/schjpq.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/schjpo.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/schjpt.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/schjpn.webp"),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null, // Initially no score
        status = GameStatus.PLANNING_TO_PLAY, // Default status,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/ar36b9.webp"
    ),
    Game (
        id = 2,
        name = "Stardew Valley",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/xrpmydnu9rpxvxfjkiu7.webp",
        trailerUrl = "https://www.youtube.com/watch?v=8A7A1X1TVNc",
        description = "Stardew Valley is an open-ended country-life RPG! You’ve inherited your grandfather’s old farm plot in Stardew Valley. Armed with hand-me-down tools and a few coins, you set out to begin your new life. Can you learn to live off the land and turn these overgrown fields into a thriving home? It won’t be easy. Ever since Joja Corporation came to town, the old ways of life have all but disappeared. The community center, once the town’s most vibrant hub of activity, now lies in shambles. But the valley seems full of opportunity. With a little dedication, you might just be the one to restore Stardew Valley to greatness!",
        platforms = listOf("Android", "iOS", "Linux", "Mac", "Nintendo Switch", "PC (Microsoft Windows)", "PlayStation 4", "PlayStation Vita", "Wii U", "Xbox One"),
        releaseDate = "2016",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/rhxs1x9w5hf5kde2osf5.webp"
            ,"https://images.igdb.com/igdb/image/upload/t_720p/iwswpvxa9ytrpk8yjcyx.webp"
            ,"https://images.igdb.com/igdb/image/upload/t_720p/sw7rtba7p1xs77klsime.webp"
            ,"https://images.igdb.com/igdb/image/upload/t_720p/g1aakqbkp2quq0krqeky.webp"
            ,"https://images.igdb.com/igdb/image/upload/t_720p/g1aakqbkp2quq0krqeky.webp"
        ),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/ar5l8.webp"
    ),
    Game (
        id = 3,
        name = "Disco Elysium",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co1sfj.webp",
        trailerUrl = "https://www.youtube.com/watch?v=SEJOJ4AUBic",
        description = "A CRPG in which, waking up in a hotel room a total amnesiac with highly opinionated voices in his head, a middle-aged detective on a murder case inadvertently ends up playing a part in the political dispute between a local labour union and a larger international body, all while struggling to piece together his past, diagnose the nature of the reality around him and come to terms with said reality.",
        platforms = listOf("Mac", "PC (Microsoft Windows)"),
        releaseDate = "2019",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/jgfwlctsfh8yljnjdeab.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/qfml0sjrmeiv5gf6tgg1.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/ar4m6.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/ar4m8.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/ar4m5.webp"),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/ar4m9.webp"
    ),
    Game (
        id = 4,
        name = "Deltarune",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co3w7g.webp",
        trailerUrl = "https://www.youtube.com/watch?v=9HjcVhf54YI",
        description = "UNDERTALE's parallel story, DELTARUNE. Meet new and old characters in a tale that steps closer to its end, chapter by chapter. Dodge bullets in nonviolent RPG battles as you listen to funky, funky music.",
        platforms = listOf("Mac", "Nintendo Switch", "PC (Microsoft Windows)", "PlayStation 4"),
        releaseDate = "TBD",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/scdl5l.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scdl5m.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scdl5k.webp"),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/ar17go.webp"
    ),
    Game (
        id = 5,
        name = "The Last Of Us Part I",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co5xex.webp",
        trailerUrl = "https://www.youtube.com/watch?v=LW5NwaUXgIA",
        description = "Experience the emotional storytelling and unforgettable characters of Joel and Ellie in The Last of Us, winner of over 200 Game of the Year awards and now rebuilt for PlayStation 5.\n" +
                "\n" +
                "Enjoy a total overhaul of the original experience, faithfully reproduced but incorporating modernized gameplay, improved controls and expanded accessibility options. Plus, feel immersed with improved effects and enhanced exploration and combat.\n" +
                "\n" +
                "It also includes the Left Behind story DLC.",
        platforms = listOf("PC (Microsoft Windows)", "Playstation 5"),
        releaseDate = "2022",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/schcg6.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/schcg7.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scq0k5.webp"),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/ar1oia.webp"
    ),
    Game (
        id = 6,
        name = "Omori",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co1xlp.webp",
        trailerUrl = "https://www.youtube.com/watch?v=nV0BST2nifk&t=8s&ab_channel=OMOCAT",
        description = "A turn-based surreal horror RPG in which a child traverses various mundane, quirky, humourous, mysterious and horrific lands with his friends in search of a missing person while confronting his past and his fears. Explore a strange world full of colorful friends and foes. When the time comes, the path you’ve chosen will determine your fate... and perhaps the fate of others as well.",
        platforms = listOf("Mac", "Nintendo 3DS", "Nintendo Switch", "PC (Microsoft Windows)", "Playstation 4", "Playstation Vita", "Xbox One", "Xbox Series X|S"),
        releaseDate = "2020",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/sc752s.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc752r.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc752t.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc752v.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc752u.webp"),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        userScore = null,
        isReminded = mutableStateOf(false),
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/arive.webp"
    ),
    Game (
        id = 7,
        name = "Spiritfarer",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co2fe7.webp",
        trailerUrl = "https://www.youtube.com/watch?v=Xu4JHmcfrtw&t=14s&ab_channel=ThunderLotus",
        description = "Spiritfarer is a cozy management game about dying. You play Stella, ferrymaster to the deceased, a Spiritfarer. Build a boat to explore the world, then befriend and care for spirits before finally releasing them into the afterlife. Farm, mine, fish, harvest, cook, and craft your way across mystical seas. Join the adventure as Daffodil the cat, in two-player cooperative play. Spend relaxing quality time with your spirit passengers, create lasting memories, and, ultimately, learn how to say goodbye to your cherished friends. What will you leave behind?",
        platforms = listOf("Google Stadia", "Linux", "Mac", "Nintendo Switch", "PC (Microsoft Windows)", "Playstation 4", "Xbox One"),
        releaseDate = "2020",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/sc6lcc.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc6lca.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc6lcb.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc6lc8.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc6lc7.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc6lc9.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc6lcd.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sc6lce.webp"),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/ar8eu.webp"
    ),
    Game (
        id = 8,
        name = "Vampire Survivors",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co4bzv.webp",
        trailerUrl = "https://www.youtube.com/watch?v=6HXNxWbRgsg",
        description = "Mow thousands of night creatures and survive until dawn! Vampire Survivors is a gothic horror casual game with rogue-lite elements, where your choices can allow you to quickly snowball against the hundreds of monsters that get thrown at you.",
        platforms = listOf("Android", "iOS", "Mac", "Nintendo Switch", "PC (Microsoft Windows)", "Playstation 4", "Playstation 5", "Xbox One", "Xbox Series X|S"),
        releaseDate = "2022",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/scf8q1.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scf8q2.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scf8q3.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scf8q4.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scf8q0.webp"),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/ar1cnh.webp"
    ),
    Game (
        id = 9,
        name = "Hollow Knight",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co93cr.webp",
        trailerUrl = "https://www.youtube.com/watch?v=Y2amTl5lBYM",
        description = "A 2D metroidvania with an emphasis on close combat and exploration in which the player enters the once-prosperous now-bleak insect kingdom of Hallownest, travels through its various districts, meets friendly inhabitants, fights hostile ones and uncovers the kingdom's history while improving their combat abilities and movement arsenal by fighting bosses and accessing out-of-the-way areas.",
        platforms = listOf("Linux", "Mac", "Nintendo Switch", "PC (Microsoft Windows)", "Wii U"),
        releaseDate = "2017",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/p3svrq6ewzxnn7p1a3v9.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/ityinxmtkakwbokpcwws.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/bkgxmg2m4h8wf5g9tblh.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/a3f72xprqkfuqdmha5ks.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/q634ullxbvipm6q6mcq9.webp"),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/ylrp6zuf9e7tcu1nvuir.webp"
    ),
    Game (
        id = 10,
        name = "The Witcher IV",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co95i0.webp",
        trailerUrl = "https://www.youtube.com/watch?v=54dabgZJ5YA&t=4s&ab_channel=TheWitcher",
        description = "The Witcher IV is a single-player, open-world RPG from CD PROJEKT RED. At the start of a new saga, players take on the role of Ciri, a professional monster slayer, and embark on a journey through a brutal dark-fantasy world.",
        platforms = listOf("PC (Microsoft Windows)", "Playstation 5", "Xbox Series X|S"),
        releaseDate = "TBD",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/scus48.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scus49.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scus4a.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scuxde.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/scuxdf.webp"),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/ar38va.webp"
    ),
    Game (
        id = 11,
        name = "Persona 3 Reload",
        imageUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co6z12.webp",
        trailerUrl = "https://www.youtube.com/watch?v=4py4V5xwXWE",
        description = "Step into the shoes of a transfer student thrust into an unexpected fate when entering the hour \"hidden\" between one day and the next. Awaken an incredible power and chase the mysteries of the Dark Hour, fight for your friends, and leave a mark on their memories forever.\n" +
                "\n" +
                "Persona 3 Reload is a captivating reimagining of the genre-defining RPG, reborn for the modern era.",
        platforms = listOf("PC (Microsoft Windows)", "Playstation 4", "Playstation 5", "Xbox One", "Xbox Series X|S"),
        releaseDate = "2024",
        screenshots = listOf("https://images.igdb.com/igdb/image/upload/t_720p/scmww8.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sco1d3.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sco1d4.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sco1d8.webp",
            "https://images.igdb.com/igdb/image/upload/t_720p/sco1da.webp"),
        isFavorite = mutableStateOf(false),
        isAddedToList = mutableStateOf(false),
        isReminded = mutableStateOf(false),
        userScore = null,
        status = GameStatus.PLANNING_TO_PLAY,
        artworkUrl = "https://images.igdb.com/igdb/image/upload/t_720p/ar2gyi.webp"
    )
)