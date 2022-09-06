package com.example.ttanslateapp.presentation.modify_word.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.ttanslateapp.R
import com.example.ttanslateapp.domain.model.modify_word.ModifyWordListItem

@Composable
fun ListItem(wordListInfo: ModifyWordListItem, onItemsPress: () -> Unit, withMark: Boolean) {

    val borderColor =
        if (wordListInfo.isSelected) colorResource(id = R.color.green) else colorResource(id = R.color.blue_2)

    ConstraintLayout(
    ) {
        val (dd, mark) = createRefs()
        val modifier = if (withMark) Modifier
            .constrainAs(dd) {
                start.linkTo(parent.start, margin = 40.dp)
                end.linkTo(parent.end, margin = 40.dp)
                width = Dimension.matchParent
            }
        else
            Modifier

        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onItemsPress() }
                    .border(
                        width = 2.dp,
                        color = if (withMark) borderColor else colorResource(id = R.color.blue_2),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(vertical = 16.dp, horizontal = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = wordListInfo.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = wordListInfo.count.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        if (wordListInfo.isSelected && withMark) {
            Image(
                painter = painterResource(
                    id = R.drawable.check_mark,

                    ),
                contentDescription = stringResource(id = R.string.modify_word_cd_selected_mark),
                Modifier
                    .constrainAs(mark) {
                        start.linkTo(dd.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(dd.bottom)
                        top.linkTo(dd.top)
                    }
                    .width(20.dp),
            )

        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun ComposableListItemPreview() {
    ListItem(
        wordListInfo = ModifyWordListItem(
            title = "My List",
            count = 10,
            id = 1L
        ),
        onItemsPress = {},
        withMark = false,
    )
}